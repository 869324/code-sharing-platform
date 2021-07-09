package platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import freemarker.template.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
public class CodeSharingPlatform {
    private static List<Map> sequence = new ArrayList<>();
    private static Configuration cfg;
    private static Writer writer;

    @GetMapping(path="/code/{id}")
    public String getCode(@PathVariable int id, HttpServletResponse response) throws IOException, TemplateException {
        //Template getCode = cfg.getTemplate("getCode.ftlh");
        //getCode.process(sequence.get(id), writer);
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Code</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <span id=\"load_date\">"+sequence.get(id).get("date")+"</span>\n" +
                "    <pre id=\"code_snippet\">"+sequence.get(id).get("code")+"</pre>\n" +
                "</body>\n" +
                "</html>";
        return html;
    }

    @GetMapping(path="api/code/{id}")
    public Map getApi(@PathVariable int id, HttpServletResponse response){
        response.addHeader("Content-Type", "application/json");
        return sequence.get(id);
    }

    @PostMapping(value = "/api/code/new", consumes = "application/json")
    public Map postCode(@RequestBody HashMap json){
        LocalDate dateNow = LocalDate.now();
        LocalTime timeNow = LocalTime.now().withNano(0);
        String date = dateNow.toString() + " " + timeNow.toString();
        Map<String, String> map = new HashMap<>();
        map.put("date", date);
        map.put("code", json.get("code").toString());
        sequence.add(map);
        Map<String, String> response = new HashMap<>();
        response.put("id", String.valueOf(sequence.size() - 1));
        return response;
    }

    @GetMapping(path = "/code/new")
    public String getTextArea(){
        String html = "<html>\n" +
                "<head>\n" +
                "<title>Create</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form method=\"POST\">\n" +
                "<textarea id=\"code_snippet\" placeholder=\"Type your code here\"></textarea>\n" +
                "<button id=\"send_snippet\" type=\"submit\" onclick=\"send()\">Submit</button>\n" +
                "</form>\n" +
                "<script>function send() {\n" +
                "    let object = {\n" +
                "        \"code\": document.getElementById(\"code_snippet\").value\n" +
                "    };\n" +
                "    \n" +
                "    let json = JSON.stringify(object);\n" +
                "    \n" +
                "    let xhr = new XMLHttpRequest();\n" +
                "    xhr.open(\"POST\", '/api/code/new', false)\n" +
                "    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');\n" +
                "    xhr.send(json);\n" +
                "    \n" +
                "    if (xhr.status == 200) {\n" +
                "      alert(\"Success!\");\n" +
                "    }\n" +
                "}</script>"+
                "</body>\n" +
                "</html>";
        return html;
    }

    @GetMapping(path = "/api/code/latest")
    public List apiLatest(){
        return getLatest();
    }

    @GetMapping(path = "/code/latest")
    public String gLatest() throws IOException, TemplateException {
        /*Template getCode = cfg.getTemplate("getLatest.ftl");
        Map<String, Object> root = new HashMap<>();
        root.put("latest", getLatest());
        root.put("title", "Latest");
        getCode.process(root, writer);*/
        String html ="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Latest</title>\n" +
                "</head>\n" +
                "<body>";
        int size = sequence.size();
        for (int i = size - 1; i >= size - 10; i--){
            try{
                html += "<div><span id=\"load_date\">"+sequence.get(i).get("date")+"</span>\n" +
                        "<br><pre id=\"code_snippet\">"+sequence.get(i).get("code")+"</pre></div>";
            }catch (IndexOutOfBoundsException ex){
                break;
            }
        }
        html += "</body>\n" +
                "</html>";
        return html;

        }



    public List getLatest(){
        List<Map> latest = new ArrayList<>();
        int size = sequence.size();
        for (int i = size - 1; i >= size - 10; i--){
            try{
                latest.add(sequence.get(i));
            }catch (IndexOutOfBoundsException ex){
                break;
            }
        }
        return latest;
    }
    public static void main(String[] args) throws IOException {
        cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setDirectoryForTemplateLoading(new File("C:/Users/javan/IdeaProjects/Code Sharing Platform/Code Sharing Platform/task/src/platform/templates"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        writer = new OutputStreamWriter(System.out);

        SpringApplication.run(CodeSharingPlatform.class, args);
    }

}
