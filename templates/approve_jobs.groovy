#!/usr/bin/env groovy
/**
 * Jenkins configuration for jobs, views and folders
 *
 * @see https://github.com/jenkinsci/job-dsl-plugin/wiki/Job-DSL-Commands
 * @todo Approve scripts automatically https://github.com/openshift/jenkins-sync-plugin/issues/57
 */
job("approve-scripts") {
  description "Automatically approve steps"
  disabled(false)

  logRotator(-1, 1)

  triggers {
    cron {
      spec("*/2 * * * *")
    }
  }

  steps {
    jobDsl {
      scriptText(
        """
        import jenkins.model.Jenkins

        def approvalPrefix = ""

        def scriptApproval = Jenkins.instance.getExtensionList('org.jenkinsci.plugins.scriptsecurity.scripts.ScriptApproval')[0]
        def hashesToApprove = scriptApproval.pendingScripts.findAll{ it.script.startsWith(approvalPrefix) }.collect{ it.getHash() }
        hashesToApprove.each {
          println it
          scriptApproval.approveScript(it)
        }
        """.stripIndent().trim()
      )
    }
  }
}