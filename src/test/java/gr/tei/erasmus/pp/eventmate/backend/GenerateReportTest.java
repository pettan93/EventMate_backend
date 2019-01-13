package gr.tei.erasmus.pp.eventmate.backend;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.ReportInfoDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.ReportRequestDTO;
import gr.tei.erasmus.pp.eventmate.backend.enums.EventState;
import gr.tei.erasmus.pp.eventmate.backend.enums.ReportType;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.utils.DateUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class GenerateReportTest {


    private HashMap<Long, User> mockUsers = new HashMap();

    private HashMap<Long, Task> mockTasks = new HashMap();


    @Before
    public void mockUsers() {


        mockUsers.put(1L, new User(1L,"Petr Kalas", "petr@mail", "pass", null, 100));
        mockUsers.put(2L, new User(2L,"Anna Neco", "anna@mail", "pass", null, 100));
        mockUsers.put(3L, new User(3L,"Artur Dvacet", "klara@mail", "pass", null, 100));
        mockUsers.put(4L, new User(4L,"Petra A", "petra@mail", "pass", null, 100));
        mockUsers.put(5L, new User(5L,"Klara Asd", "klara@mail", "pass", null, 100));


        mockTasks.put(1L, new Task(
                1L,
                "Rakia shots",
                mockUsers.get(1L),
                Arrays.asList(mockUsers.get(2L), mockUsers.get(3L)),
                "Pub 123",
                "Drink rakia shots",
                5L,
                false,
                10));

        mockTasks.put(2L, new Task(
                2L,
                "Kiss her",
                mockUsers.get(1L),
                Collections.singletonList(mockUsers.get(3L)),
                "Pub 123",
                "Kiss a miss",
                5L,
                false,
                10));

    }

    @Test
    public void testGenerate() {


        var reportInfo = new ReportInfoDTO();
        reportInfo.setIncludeName(true); //
        reportInfo.setIncludePlace(true); //
        reportInfo.setIncludeOwner(true); //
        reportInfo.setIncludeDate(true); //

        reportInfo.setIncludeReportCreator(true); //
        reportInfo.setIncludeReportCreatedDate(true); //
        reportInfo.setListOfIncludedGuests(Arrays.asList(2L, 3L, 4L));
        reportInfo.setListOfIncludedTasks(Arrays.asList(1L, 2L));

        var reportRequest = new ReportRequestDTO();
        reportRequest.setName("The best party ever!");
        reportRequest.setComment("With this memory paper we never forget!");
        reportRequest.setType(ReportType.FULL_SUMMARY);
        reportRequest.setReportInfoDTO(reportInfo);

        var event = new Event(
                "Slavic party",
                new Date(),
                "Pub",
                Arrays.asList(mockTasks.get(1L), mockTasks.get(2L)),
                EventState.FINISHED,
                null);

        event.setEventOwner(mockUsers.get(1L));
        event.setTasks(Arrays.asList(mockTasks.get(1L), mockTasks.get(1L)));
        event.setGuests(Arrays.asList(mockUsers.get(2L), mockUsers.get(3L), mockUsers.get(4L), mockUsers.get(5L)));


        generateReport(reportRequest, event, mockUsers.get(1L));


    }

    public File generateReport(ReportRequestDTO rpr, Event event, User reportCreator) {


        switch (rpr.getType()){
            case CERTIFICATE:
                return generateCerticateReport(rpr,event,reportCreator);
            case FULL_SUMMARY:
                return generateFullSummaryReport(rpr,event,reportCreator);
        }


        return null;
    }

    public File generateCerticateReport(ReportRequestDTO rpr, Event event, User reportCreator) {

        File saveFile = new File("reports/out2.pdf");
        PDDocument document = null;
        try {
            document = PDDocument.load(new File("reports/template_certificate.pdf"));
            PDPage page = (PDPage) document.getDocumentCatalog().getPages().get(0);

            PDDocumentInformation pdd = document.getDocumentInformation();
            pdd.setTitle("EventMate Certificate");

            PDPageContentStream contents = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

            //Begin the Content stream

            PDType0Font font = PDType0Font.load(document, new File("c:/windows/fonts/calibri.ttf"));
            contents.setStrokingColor(Color.GRAY);

            // report name
            putText(contents, font, 24, 380 - (rpr.getName().length()*5) + 10, 320, rpr.getName());

            // event name
            if(rpr.getReportInfoDTO().getIncludeName()){
                putText(contents, font, 14, 380 - (event.getName().length()*5) + 30, 290, event.getName());
            }


            // report comment
            putText(contents, font, 16, 380 - (rpr.getComment().length()*3) - 5, 265, rpr.getComment());


            String finalString = "Created ";

            //report creator
            if (rpr.getReportInfoDTO().getIncludeReportCreator()) {
                finalString += " by " + reportCreator.getUserName();
            }

            if (rpr.getReportInfoDTO().getIncludeReportCreatedDate()) {
                finalString += " at " + DateUtils.formatDate(new Date(), "dd.MM.yyyy HH:mm");
            }

            if (rpr.getReportInfoDTO().getIncludeReportCreator() || rpr.getReportInfoDTO().getIncludeReportCreatedDate()) {
                putText(contents, font, 10, 115, 110, finalString);
            }

            //logo right top corner
            PDImageXObject pdImage = PDImageXObject.createFromFile("reports/logo3.png", document);
            contents.drawImage(pdImage, 400, 610);


            contents.close();
            document.save(saveFile.getAbsolutePath());
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return saveFile;
    }

    public File generateFullSummaryReport(ReportRequestDTO rpr, Event event, User reportCreator) {

        File saveFile = new File("reports/out1.pdf");
        PDDocument document = null;
        try {
            document = PDDocument.load(new File("reports/template_full_report.pdf"));
            PDPage page = (PDPage) document.getDocumentCatalog().getPages().get(0);

            PDDocumentInformation pdd = document.getDocumentInformation();
            pdd.setTitle("EventMate Report");

            PDPageContentStream contents = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

            //Begin the Content stream

            PDType0Font font = PDType0Font.load(document, new File("c:/windows/fonts/calibri.ttf"));
            contents.setStrokingColor(Color.GRAY);

            // report name
            putText(contents, font, 24, 120, 665, rpr.getName());
            // report comment
            putText(contents, font, 14, 120, 630, rpr.getComment());


            int offset = 600;
            // if some of event details shoul be included
            if (rpr.getReportInfoDTO().getIncludeName() ||
                    rpr.getReportInfoDTO().getIncludePlace() ||
                    rpr.getReportInfoDTO().getIncludeOwner() ||
                    rpr.getReportInfoDTO().getIncludeDate()) {

                int spaceBeetweenInfos = 15;

                putText(contents, Color.GRAY, font, 10, 130, offset, "Event details");
                contents.drawLine(120, offset - 4, 300, offset - 4);


                offset = offset - 5;

                //event name
                if (rpr.getReportInfoDTO().getIncludeName()) {
                    offset = offset - spaceBeetweenInfos;
                    putText(contents, font, 10, 120, offset, "Name: " + event.getName());
                }

                //event place
                if (rpr.getReportInfoDTO().getIncludePlace()) {
                    offset = offset - spaceBeetweenInfos;
                    putText(contents, font, 10, 120, offset, "Place: " + event.getPlace());
                }

                //event date
                if (rpr.getReportInfoDTO().getIncludeDate()) {
                    offset = offset - spaceBeetweenInfos;
                    putText(contents, font, 10, 120, offset, "Date: " + event.getDate());
                }

                //event owner
                if (rpr.getReportInfoDTO().getIncludeOwner()) {
                    offset = offset - spaceBeetweenInfos;
                    putText(contents, font, 10, 120, offset, "Owner: " + event.getEventOwner().getUserName());
                }
            }


            // task section
            putText(contents, Color.GRAY, font, 14, 210, offset - 35, "Tasks");
            PDImageXObject tasksicon = PDImageXObject.createFromFile("reports/tasks-icon.png", document);
            contents.drawImage(tasksicon, 180, offset - 45);

            int taskOffset = offset + 5;
            for (Task task : event.getTasks()) {
                if (rpr.getReportInfoDTO().getListOfIncludedTasks().contains(task.getId())) {
                    taskOffset = taskOffset - 70;
                    putText(contents, font, 13, 120, taskOffset, task.getName());
                    putText(contents, font, 10, 125, taskOffset - 20, "Description: " + task.getDescription());
                    putText(contents, font, 10, 125, taskOffset - 35, "Place: " + task.getPlace());
                }
            }


            // user section
            putText(contents, Color.GRAY, font, 14, 380, offset - 35, "Guests");
            PDImageXObject guestsicon = PDImageXObject.createFromFile("reports/guests-icon.png", document);
            contents.drawImage(guestsicon, 350, offset - 45);

            int userOffset = offset - 40;
            for (User user : event.getGuests()) {
                if (rpr.getReportInfoDTO().getListOfIncludedGuests().contains(user.getId())) {
                    userOffset = userOffset - 25;
                    putText(contents, font, 13, 350, userOffset, user.getUserName());
                }
            }




            String finalString = "Created ";
            //report creator
            if (rpr.getReportInfoDTO().getIncludeReportCreator()) {
                finalString += " by " + reportCreator.getUserName();
            }

            if (rpr.getReportInfoDTO().getIncludeReportCreatedDate()) {
                finalString += " at " + DateUtils.formatDate(new Date(), "dd.MM.yyyy HH:mm");
            }

            if (rpr.getReportInfoDTO().getIncludeReportCreator() || rpr.getReportInfoDTO().getIncludeReportCreatedDate()) {
                putText(contents, font, 10, 115, 148, finalString);
            }

            //logo right top corner
            PDImageXObject pdImage = PDImageXObject.createFromFile("reports/logo3.png", document);
            contents.drawImage(pdImage, 400, 610);


            contents.close();
            document.save(saveFile.getAbsolutePath());
            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return saveFile;
    }


    public PDPageContentStream putText(PDPageContentStream contents, PDFont font, Integer size, Integer x, Integer y, String text) throws IOException {
        return putText(contents, Color.BLACK, font, size, x, y, text);
    }

    public PDPageContentStream putText(PDPageContentStream contents, Color color, PDFont font, Integer size, Integer x, Integer y, String text) throws IOException {
        contents.beginText();
        contents.setFont(font, size);
        contents.newLineAtOffset(x, y);
        contents.setNonStrokingColor(color);
        contents.showText(text);
        contents.endText();
        return contents;
    }


}
