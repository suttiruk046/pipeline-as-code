#!/usr/bin/env groovy

def deployment_pipelines = [
  [
    repository: "suttiruk046/sb-helloworld",
    jobfolder: "ktaxa",
    jobname: "sb-helloworld",
    nodever: "java8",
    osbc: "sb-helloworld",
    osproject: "company.sb-helloworld"
  ]
].each { i ->
  if (i.containsKey('jobfolder')){
    farr = i['jobfolder'].split("/")
    len = farr.length
    switch(len) {
      case 1:
      folder("${farr[0]}")
      break
      case 2:
      folder("${farr[0]}")
      folder("${farr[0]}/${farr[1]}")
      break
      case 3:
      folder("${farr[0]}")
      folder("${farr[0]}/${farr[1]}")
      folder("${farr[0]}/${farr[1]}/${farr[2]}")
      break
      default:
      folder("${farr[0]}")
      break
    }
  }

  repository    = i['repository']
  branch        = i.containsKey('branch') ? i['branch'].toLowerCase() : 'master'
  job_folder    = i['jobfolder']
  job_name      = i['jobname']
  nodever       = i.containsKey('nodever') ? i['nodever'] : 'java8'
  cred          = i.containsKey('cred') ? i['cred'] : ''
  osbc          = i.containsKey('osbc') ? i['osbc'] : ''
  osproject     = i.containsKey('osproject') ? i['osproject'] : ''
  tags          = i.containsKey('tags') ? i['tags'] : ''
  template      = i.containsKey('template') ? i['template'] : 'Jenkinsfile'
  buildSteps    = i.containsKey('buildSteps') ? i['buildSteps'] : ''
  publish       = i.containsKey('publish') ? i['publish'] : ''

  pipelineJob("${job_folder}/${job_name}") {
    description "Pipeline for ${job_name}"
    disabled(false)
    concurrentBuild(false)
    logRotator(-1, 5)

    properties {
      githubProjectUrl("https://github.com/${repository}")
    }

    triggers {
      gitHubPushTrigger()
    }

    authorization {}

    definition {
      cps {
        sandbox(true)
        script("""\
          def jf
          node(){
              checkout changelog: false, poll: false, scm: [\$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [[\$class: 'RelativeTargetDirectory', relativeTargetDir: 'jenkinsfile'], [\$class: 'IgnoreNotifyCommit'], [\$class: 'WipeWorkspace']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'github-com', url: 'https://github.com/suttiruk046/pipeline-as-code.git']]]
              checkout changelog: true, scm: [\$class: 'GitSCM', branches: [[name: '*/${branch}']], doGenerateSubmoduleConfigurations: false, extensions: [[\$class: 'RelativeTargetDirectory', relativeTargetDir: 'src'], [\$class: 'WipeWorkspace']], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'github-com', url: 'https://github.com/${repository}.git']]]
              jf = load 'jenkinsfile/company/templates/${template}'
              jf.defaultPipeline('${nodever}', '${cred}', '${osbc}', '${osproject}', '${tags}', '${buildSteps}', '${publish}')
          }
          """.stripIndent().trim()
        )
      }
    }
  }
}
