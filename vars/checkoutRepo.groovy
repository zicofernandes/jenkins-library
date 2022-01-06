def call(Closure body) {
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
  def scmInfo
  checkoutRepo(config)
}

def call(Map config) {
  checkoutRepo(config)
}

def checkoutRepo(config) {
  def url_ = config.get('url', "")
  def branch_ = config.branch
  def credentialsId_ = config.credentialsId
 
  timestamps {
    ansiColor('xterm') {
      if(!url_) {
        echo "[INFO]: using basic checkout scm since url not defined"
      }

      if(!branch_) {
        branch_ = 'master'
      }
      deleteDir()
      if(url_) {
        git url: url_, branch: branch_, credentialsId: credentialsId_
      } else {
        this.scmInfo = checkout scm
        scmInfo.each{k, v -> env."${k}" = v}
      }
    }
  }
}
