package br.gov.pmr.cad_familias.controller.relatorio;

import br.gov.pmr.cad_familias.domain.familia.Familia;
import br.gov.pmr.cad_familias.repository.familia.FamiliaRepository;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/relatorios")
public class RelatorioController {

    @Autowired
    private final FamiliaRepository familiaRepository;

    public RelatorioController(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
    }

    @GetMapping("/familias")
    public ResponseEntity<byte[]> gerarRelatorioFamilias() {
        try {
            // 1. Buscar dados reais do banco
            List<Familia> familias = familiaRepository.findAll();

            // 2. Carregar o template .jrxml
            InputStream reportStream = getClass().getResourceAsStream("/relatorios/familias.jrxml");
//            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 3. Preencher com os dados
//            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(familias);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

            // 4. Exportar para PDF
//            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // 5. Configurar headers HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "familias.pdf");

            // 6. Retornar resposta
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
