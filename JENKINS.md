# Jenkins registration pipeline

## Configured local job

The local job is `http://localhost:8080/job/OSA-Practice-Automation/`.
Its inline pipeline is saved in `Jenkinsfile.local`. It uses the built-in Windows
node and the installed Java 21/Maven paths, copies the current local automation
project into a separate Jenkins workspace, and runs the suite there. Use
**Build with Parameters** (or **Build Now** on the initial run) to run it again.
This local job does not poll GitHub. The root `Jenkinsfile` below is the SCM-based
version to use after committing and pushing the project changes.

The root `Jenkinsfile` checks out this automation repository, compiles it, installs
Playwright Firefox and WebKit, checks the running application's registration page,
and executes `testng-practice.xml` headlessly. TestNG runs its browser tests in
parallel. Results and artifacts appear on the Jenkins build page.

## One-time setup

1. Run a Windows Jenkins agent on the machine hosting Mobile User. Give it the
   label `mobile-user-windows`. `localhost` always refers to the agent machine.
2. Install Jenkins plugins: Pipeline, Git, JUnit, and Pipeline: Nodes and Processes
   (for the PowerShell step). Use a supported Java runtime for Jenkins itself.
3. Under **Manage Jenkins > Tools**, configure a JDK 21 installation named `JDK21`
   and Maven named `Maven3`. Git must be available on the agent's PATH. The
   automation targets Java 17 and can be compiled by JDK 21.
4. Install Google Chrome and Microsoft Edge for the Jenkins agent account/machine.
   The pipeline installs Playwright's Firefox and WebKit for that account.
5. Keep Mobile User and its database running. The application currently comes from
   `C:\Users\mahfu\Desktop\back_UP_xampp_files\Mobile_User_V_1.8\Mobile_User_SpringBoot`
   and `git@github.com:MahfuzAhmed2020/Mobile_User_V_1.8.git`. Ensure the database
   already contains `abdul@google.com`: this suite checks duplicate registration.
6. Commit and push `Jenkinsfile` and the related automation changes to
   `https://github.com/MahfuzAhmed2020/OSA_Playwright_Automation.git`.
7. Create a Jenkins **Pipeline** job. Choose **Pipeline script from SCM > Git**.
   Enter the automation repository URL, select its actual branch, and set
   **Script Path** to `Jenkinsfile`. Add a Jenkins Git credential if the repository
   is private. Do not place tokens or private keys in the repository.
8. Click **Build Now** once. Subsequent automation commits are detected by SCM
   polling approximately every five minutes. Application repository commits alone
   do not trigger this job.

## Running and viewing results

The job parameter `APP_BASE_URL` defaults to `http://localhost:8087`. Set it to a
reachable application address when the server is elsewhere.

The pipeline runs the equivalent of:

```powershell
mvn test "-Dsurefire.suiteXmlFiles=testng-practice.xml" "-Dheadless=true" "-DkeepOpen=false" "-Dslow.mo=0" "-Dapp.base.url=http://localhost:8087"
```

CI explicitly disables keeping browsers open so Maven can finish and publish
reports. Your XML can still enable it for local runs.

Open a Jenkins build's **Test Result**, **Console Output**, or **Artifacts**.
Reports are generated in `target/surefire-reports/`. Screenshots are archived if
present; this pipeline does not add screenshot capture to the practice tests.

## Deployment boundary

This job provides continuous integration and browser regression testing against
the already-running application. It does not build, restart, or deploy the Spring
Boot repository, and does not prove the running server matches its latest commit.
A deployment job should invoke this pipeline after deploying a chosen application
revision, with `APP_BASE_URL` set to that environment. The application build needs
Java 21. Deployment service management and database configuration must be defined
before adding automatic application replacement.
