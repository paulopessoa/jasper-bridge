require "jasper-bridge/version"
require "generator" 

module Jasper
  module Bridge

  protected
  
    def cache_hack
      if ENV['HTTP_USER_AGENT'] =~ /msie/i
        response.headers['Pragma'] = ''
        response.headers['Cache-Control'] = ''
      else
        response.headers['Pragma'] = 'no-cache'
        response.headers['Cache-Control'] = 'no-cache, must-revalidate'
      end
    end

    def send_doc(xml, xml_start_path, report, filename, output_type = 'pdf')
      mime_type = "application/#{output_type}"

      cache_hack
      send_data Generator.generate_report(xml, report, output_type, xml_start_path),
        :filename => "#{filename}.#{output_type}", 
        :type => mime_type, 
        :disposition => 'inline' #'attachment'
    end

    def self.teste
      p 'dir=' + File.expand_path("../../",__FILE__)
    end
    
  end
end
