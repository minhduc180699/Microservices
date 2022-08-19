package com.saltlux.deepsignal.web.service.template;

import static com.saltlux.deepsignal.web.util.TemplateConstant.*;

import com.saltlux.deepsignal.web.domain.InquiryAnswerEmail;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(@Qualifier("templateEngine") TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(InquiryAnswerEmail dataMap) {
        Context context = new Context();
        context.setVariable("entity", dataMap);
        String template = INQUIRY_ANSWER_EMAIL_TEMPLATE;
        if (Objects.nonNull(template)) {
            return templateEngine.process(template, context);
        }

        return null;
    }
}
