# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)

require "jasper-bridge/version"

Gem::Specification.new do |s|
  s.name        = "jasper-bridge"
  s.version     = Jasper::Bridge::VERSION
  s.authors     = ["Paulo Pessoa"]
  s.email       = ["paulopessoa@gmail.com"]
  s.homepage    = "https://github.com/paulopessoa/jasper-bridge"
  s.summary     = %q{Bridge from RoR to Jasper by IO.popen}
  s.description = %q{Bridge from RoR to Jasper by IO.popen}

  s.rubyforge_project = "jasper-bridge"

#  s.files         = `git ls-files`.split("\n")
  s.files       = Dir["{lib/**/*.rb,README.rdoc,test/**/*.rb,Rakefile,*.gemspec,jasper/bin/**,jasper/lib/**}"]

  s.test_files    = `git ls-files -- {test,spec,features}/*`.split("\n")
#  s.test_files    = Dir["{test/*}"]

#  s.executables   = `git ls-files -- bin/*`.split("\n").map{ |f| File.basename(f) }
#  s.executables = ["jb"]

  s.require_paths = ["lib"]
end
