#!/usr/bin/env groovy

def entities = [
  
  [
    name: "company"
  ]
].each { i ->

  entity = i['name']
  folder("${entity}")

  job("${entity}/seed_job") {
    description "Seed Job for ${entity}"
    disabled(false)
    concurrentBuild(false)
    logRotator(-1, 5)
    scm {
      git {
        remote {
          url('https://github.com/suttiruk046/pipeline-as-code.git')
          credentials('github-token')
        }
        branch('master')
      }
    }
    steps {
      jobDsl {
        targets("${entity}/seeds/*.groovy")
        sandbox(false)
        ignoreExisting(false)
      }
    }
  }
}