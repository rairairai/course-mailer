package com.odde.mailsender.controller;

import com.odde.mailsender.data.AddressItem;
import com.odde.mailsender.form.ContactListForm;
import com.odde.mailsender.form.MailSendForm;
import com.odde.mailsender.service.AddressBookService;
import com.odde.mailsender.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ContactListController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/contact-list")
    public String getContactList(@ModelAttribute("form") ContactListForm form, Model model) {

        List<AddressItem> addressList = addressBookService.get();
        model.addAttribute("contactList", addressList);
        return "contact-list";
    }

    @PostMapping("/contact-list")
    public String addContactList(@Valid @ModelAttribute("form") ContactListForm form, BindingResult result, Model model) {

//        if(Validator.isNotEmpty(form.getAddress()) && !Validator.isMailAddress(form.getAddress())) {
//            result.rejectValue("address","not.Mail", "not email format");
//        }

        AddressItem input = new AddressItem(form.getAddress(), form.getName());
        try {
            addressBookService.add(input);
        } catch (Exception e) {
            result.rejectValue("","", e.getMessage());
        }
        List<AddressItem> addressList = addressBookService.get();
        model.addAttribute("contactList", addressList);
        return "contact-list";
    }

    @PostMapping("/create-mail")
    public String createEmail(@RequestParam(required = false) String[] mailAddress, Model model) {

        model.addAttribute("address", joinMailAddress(mailAddress));
        model.addAttribute("form", new MailSendForm());

        return "send";
    }

    private String joinMailAddress(String[] mailAddress) {
        return mailAddress == null ? "" : String.join(";", mailAddress);
    }

}
