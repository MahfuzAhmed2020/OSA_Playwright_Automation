pipeline {
    // Assign this label to the Windows agent running Mobile User on port 8087.
    agent { label 'mobile-user-windows' }

    options {
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    // Configure these names under Manage Jenkins > Tools.
    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    parameters {
        string(name: 'APP_BASE_URL', defaultValue: 'http://localhost:8087',
            description: 'Running Mobile User application reachable from this Windows agent.')
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        CI = 'true'
        APP_BASE_URL = "${params.APP_BASE_URL ?: 'http://localhost:8087'}"
    }

    stages {
        stage('Checkout automation') {
            steps {
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                bat 'mvn -B -ntp clean test-compile'
            }
        }

        stage('Install Playwright browsers') {
            steps {
                // Chrome and Edge must already be installed on the agent.
                bat 'mvn -B -ntp org.codehaus.mojo:exec-maven-plugin:3.5.0:java "-Dexec.mainClass=com.microsoft.playwright.CLI" "-Dexec.args=install firefox webkit"'
            }
        }

        stage('Check application') {
            steps {
                powershell '''
                    $ErrorActionPreference = 'Stop'
                    $registrationUrl = $env:APP_BASE_URL.TrimEnd('/') + '/register'
                    $ready = $false
                    for ($attempt = 1; $attempt -le 12; $attempt++) {
                        try {
                            $response = Invoke-WebRequest -Uri $registrationUrl -UseBasicParsing -TimeoutSec 5
                            if ($response.StatusCode -eq 200 -and $response.Content.Contains('id="authForm"')) {
                                $ready = $true
                                break
                            }
                        } catch {
                            Write-Host "Waiting for Mobile User (attempt $attempt/12)..."
                        }
                        Start-Sleep -Seconds 5
                    }
                    if (!$ready) { throw "Registration page unavailable: $registrationUrl. Start Mobile User and its database on this agent." }
                '''
            }
        }

        stage('Parallel registration tests') {
            steps {
                // TestNG controls browser parallelism; CI overrides the local keep-open setting.
                powershell '''
                    $ErrorActionPreference = 'Stop'
                    & mvn -B -ntp test '-Dsurefire.suiteXmlFiles=testng-practice.xml' '-Dheadless=true' '-DkeepOpen=false' '-Dslow.mo=0' "-Dapp.base.url=$env:APP_BASE_URL"
                    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
                '''
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/TEST-*.xml', allowEmptyResults: false
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/surefire-reports/**/*,target/screenshots/**/*',
                allowEmptyArchive: true
        }
    }
}
