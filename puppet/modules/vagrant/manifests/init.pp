class vagrant::clojure (
  $lein_version = $::vagrant::params::lein_version
) inherits vagrant::params {

  # Validate parameters
  if ($lein_version == undef or $lein_version == '') {
    fail('Required lein_version parameter is not set.')
  }

  if ! defined(Package['git']) {
    package { 'git':
      ensure => installed
    }
  }

  $lein = '/usr/local/bin/lein'
  $uri  = "https://raw.githubusercontent.com/technomancy/leiningen/${lein_version}/bin/lein"

  package { 'java-1.8.0-openjdk':
    ensure => 'installed',
  }

  package { 'java-1.8.0-openjdk-devel':
    ensure => 'installed',
  }

  exec { 'get-leiningen':
    path    => '/bin:/sbin:/usr/bin:/usr/sbin:/usr/local/bin',
    command => "curl --fail -sL ${uri} > ${lein}",
    creates => $lein,
  }

  file { $lein:
    mode    => '0755',
  }

  Package['java-1.8.0-openjdk'] -> Package['java-1.8.0-openjdk-devel'] -> Exec['get-leiningen'] -> File[$lein]
}
