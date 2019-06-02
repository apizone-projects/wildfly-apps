
package io.apizone.wildfly.service;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import io.apizone.wildfly.data.CustomerListProducer;
import org.hibernate.Session;
import io.apizone.wildfly.model.Customer;
import java.util.List;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 * Registers a Customer
 * <p>
 * <p>
 * The @Stateless annotation eliminates the need for manual transaction demarcation
 * </p>
 */

@Stateless
public class CustomerRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Customer> customerEventSrc;

    @Inject
    CustomerListProducer customerListProducer;


    public void register(Customer customer) throws Exception {
        log.info("Registering " + customer.getName());
        // em.persist(customer);

        // using Hibernate session(Native API) and JPA entitymanager
        Session session = (Session) em.getDelegate();
        session.persist(customer);
        customerEventSrc.fire(customer);
    }


    public void pdfDownload() throws Exception {
        List<Customer> customersList = customerListProducer.getCustomers();
        int customerListSize = customersList.size();
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage( page );
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);

        String[][] content = new String[customerListSize + 1][4];
        content[0][0] = "Id";
        content[0][1] = "Name";
        content[0][2] = "Phone Number";
        content[0][3] = "Address";

        for(int i=0; i < customerListSize; i++){
            Customer customer = customersList.get(i);
            log.info("------- Customer " + (i+1) + ": " + customer.getName());

            content[i+1][0] =  customer.getId().toString();
            content[i+1][1] =  customer.getName();
            content[i+1][2] =  customer.getPhoneNumber();
            content[i+1][3] =  customer.getAddress();
        }


        drawTable(page, contentStream, 700, 100, content);
        contentStream.close();
        doc.save("/tmp/test.pdf" );

        System.out.println("------- PDF created");
        doc.close();

    }



    public static void drawTable(PDPage page, PDPageContentStream contentStream, float y, float margin,
        String[][] content) throws IOException {
        final int rows = content.length;
        final int cols = content[0].length;
        final float rowHeight = 20f;
        final float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
        final float tableHeight = rowHeight * rows;
        final float colWidth = tableWidth / (float) cols;
        final float cellMargin = 5f;

        // draw the rows
        float nexty = y;
        for (int i = 0; i <= rows; i++) {
            contentStream.drawLine(margin, nexty, margin + tableWidth, nexty);
            nexty -= rowHeight;
        }

        // draw the columns
        float nextx = margin;
        for (int i = 0; i <= cols; i++) {
            contentStream.drawLine(nextx, y, nextx, y - tableHeight);
            nextx += colWidth;
        }

        // now add the text
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

        float textx = margin + cellMargin;
        float texty = y - 15;
        for (int i = 0; i < content.length; i++) {
            for (int j = 0; j < content[i].length; j++) {
                String text = content[i][j];
                contentStream.beginText();
                contentStream.moveTextPositionByAmount(textx, texty);
                contentStream.drawString(text);
                contentStream.endText();
                textx += colWidth;
            }
            texty -= rowHeight;
            textx = margin + cellMargin;
        }
    }
}
