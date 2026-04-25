pipeline {
    agent {
        docker {
            image 'markhobson/maven-chrome'
            args '-u root:root -v /var/lib/jenkins/.m2:/root/.m2'
        }
    }
    
    stages {
        stage('Clone Repository') {
            steps {
                // Points Jenkins to clone YOUR actual repository:
                git branch: 'main', url: 'https://github.com/ashtarali4/Lab10-Pipeline.git'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }
    
    post {
        always {
            script {
                // Get commit author email
                sh "git config --global --add safe.directory ${env.WORKSPACE}"
                def committer = sh(
                    script: "git log -1 --pretty=format:'%ae'",
                    returnStdout: true
                ).trim()
                
                def raw = sh(
                    script: "grep -h \"<testcase\" target/surefire-reports/*.xml",
                    returnStdout: true
                ).trim()
                
                int total = 0
                int passed = 0
                int failed = 0
                int skipped = 0
                
                def details = ""
                
                raw.split('\n').each { line ->
                    if (line.trim() != "") {
                        total++
                        
                        // Added safe check for matcher so Jenkins won't crash if XML is malformed
                        def matcher = (line =~ /name="([^"]+)"/)
                        def name = matcher ? matcher[0][1] : "Unknown_Test"
                        
                        if (line.contains("<failure")) {
                            failed++
                            details += "${name} - FAILED\n"
                        } else if (line.contains("<skipped") || line.contains("</skipped>")) {
                            skipped++
                            details += "${name} - SKIPPED\n"
                        } else {
                            passed++
                            details += "${name} - PASSED\n"
                        }
                    }
                }
                
                def emailBody = """
Test Summary (Build #${env.BUILD_NUMBER})

Total Tests:    ${total}
Passed:         ${passed}
Failed:         ${failed}
Skipped:        ${skipped}

Detailed Results:
${details}
"""
                emailext(
                    to: committer,
                    subject: "Build #${env.BUILD_NUMBER} Test Results",
                    body: emailBody
                )
            }
        }
    }
}
