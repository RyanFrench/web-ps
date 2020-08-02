VAGRANTFILE_API_VERSION = '2'

VM_NAME    = 'web-ps'
VM_MEMORY  = 2048
VM_CPUS    = 2

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = 'centos/7'
  config.ssh.forward_agent = true

  config.vm.network "forwarded_port", guest: 8080, host: 8000

  config.vm.provider 'virtualbox' do |v|
    v.name = VM_NAME

    v.memory = VM_MEMORY
    v.cpus = VM_CPUS
  end

  config.vm.provider 'vmware_fusion' do |v|
    v.vmx['memsize'] = VM_MEMORY
    v.vmx['numvcpus'] = VM_CPUS
  end

  config.vm.provision 'docker'

  config.vm.provision 'shell',
    inline: 'echo "cd /vagrant" >> /home/vagrant/.bashrc'

  config.vm.provision 'shell',
    inline: 'rpm -ivh http://yum.puppetlabs.com/puppetlabs-release-el-7.noarch.rpm --replacepkgs && yum -y install puppet'

  config.vm.provision 'puppet' do |puppet|
    puppet.manifests_path = 'puppet'
    puppet.manifest_file  = 'manifest.pp'
    puppet.module_path = ['puppet/modules']
  end
end
