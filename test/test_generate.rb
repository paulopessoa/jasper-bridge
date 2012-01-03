require 'helper'
require 'test/unit'
require 'jasper-bridge'

class TestGenerate < Test::Unit::TestCase

  def test_generate_report_pdf
    xml_data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<produtos>\n  <produto>\n    <cod-produto>1</cod-produto>\n    <created-at>2011-09-19T14:34:15Z</created-at>\n    <nome>bolacha</nome>\n    <updated-at>2011-09-19T14:34:15Z</updated-at>\n  </produto>\n  <produto>\n    <cod-produto>2</cod-produto>\n    <created-at>2011-09-19T17:33:55Z</created-at>\n    <nome>biscoito</nome>\n    <updated-at>2011-09-19T17:33:55Z</updated-at>\n  </produto>\n</produtos>\n"
    result = Jasper::Generator.generate_report(xml_data, 'Produtos.jasper', 'pdf', '/produtos/produto')
    assert result != nil
  end

  def test_generate_report_rtf
    xml_data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<produtos>\n  <produto>\n    <cod-produto>1</cod-produto>\n    <created-at>2011-09-19T14:34:15Z</created-at>\n    <nome>bolacha</nome>\n    <updated-at>2011-09-19T14:34:15Z</updated-at>\n  </produto>\n  <produto>\n    <cod-produto>2</cod-produto>\n    <created-at>2011-09-19T17:33:55Z</created-at>\n    <nome>biscoito</nome>\n    <updated-at>2011-09-19T17:33:55Z</updated-at>\n  </produto>\n</produtos>\n"
    result = Jasper::Generator.generate_report(xml_data, 'Produtos.jasper', 'rtf', '/produtos/produto')
    assert result != nil
  end

end
