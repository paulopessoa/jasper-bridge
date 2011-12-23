# document.rb
module Jasper
  
  class Generator
    include Config
  
    def self.generate_report(xml_data, report_design, output_type, select_criteria)
      report_design << '.jasper' if !report_design.match(/\.jasper$/)
      classpath=Dir.getwd+"/jasper/bin" 
        
      case CONFIG['host']
      when /mswin32/,/mingw32/
        Dir.foreach("jasper/lib") do |file|
          classpath << ";#{Dir.getwd}/jasper/lib/"+file if (file != '.' and file != '..' and file.match(/.jar/))
        end
      else
        Dir.foreach("jasper/lib") do |file|
          classpath << ":#{Dir.getwd}/jasper/lib/"+file if (file != '.' and file != '..' and file.match(/.jar/))
        end
      end
        
      pipe = IO.popen "java -cp \"#{classpath}\" XmlJasperInterface -o#{output_type} -freports/#{report_design} -x#{select_criteria}", "w+b" 
      pipe.write xml_data
      pipe.close_write
      result = pipe.read
      pipe.close
      result
    end
    
  end

end