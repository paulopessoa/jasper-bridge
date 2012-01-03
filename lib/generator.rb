module Jasper
  
  class Generator
    include Config
  
    def self.generate_report(xml_data, report_design, output_type, select_criteria)
      report_design << '.jasper' if !report_design.match(/\.jasper$/)
      
      dir = File.expand_path("../../",__FILE__)
      classpath = "#{dir}/jasper/bin"
        
      case CONFIG['host']
      when /mswin32/,/mingw32/
        Dir.foreach("#{dir}/jasper/lib") do |file|
          classpath << ";#{dir}/jasper/lib/"+file if (file != '.' and file != '..' and file.match(/\.jar$/))
        end
      else
        Dir.foreach("#{dir}/jasper/lib") do |file|
          classpath << ":#{dir}/jasper/lib/"+file if (file != '.' and file != '..' and file.match(/\.jar$/))
        end
      end
        
      pipe = IO.popen "java -cp \"#{classpath}\" XmlJasperInterface -o:#{output_type} -f:reports/#{report_design} -x:#{select_criteria}", "w+b" 
      pipe.write xml_data
      pipe.close_write
      result = pipe.read
      pipe.close
      result
    end
    
  end

end