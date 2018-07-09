## Run OpenShift Cluster
oc cluster up

## Install Jenkins 
Resources are in openshift.
And start it.

## Install required Jenkins's plugins.
jenkins v2+ with plugins as below
1. Job DSL v1.7 with it's dependencies
2. Pipeline: Groovy with it's dependencies

## Create main pipelines
create Automatically approve pipeline scripts.
1. specific project name : approve-scripts in general tab.
2. checked Discard old builds with Strategy Log Rotation and Max # of build to keep with 1.
3. in Source Code Management, specific Repository URL with https://github.com/suttiruk046/pipelines-as-code.git with your credential.
4. in Build Triggers, checked Build periodically with */2 * * * *.
5. in Build, checked Look on Filesystem and put templates/approve_scripts.groovy in DSL Scripts.

create Master Seed pipeline script.
1. specific project name : master
2. in Source Code Management, specific Repository URL with https://github.com/suttiruk046/pipelines-as-code.git with your credential.
3. in Build Triggers, checked Build periodically with * * * * *.
4. in Build, checked Look on Filesystem and put templates/master_seed.groovy in DSL Scripts.

## Your Detailed pipelines 
detailed pipelines will be in company/seeds/*.groovy