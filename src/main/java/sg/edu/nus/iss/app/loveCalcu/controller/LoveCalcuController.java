package sg.edu.nus.iss.app.loveCalcu.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.app.loveCalcu.model.LoveResult;
import sg.edu.nus.iss.app.loveCalcu.service.LoveCalcuService;

@Controller
@RequestMapping(path = "/lovecalc")
public class LoveCalcuController {

    @Autowired
    private LoveCalcuService lcSvr;

    @GetMapping
    public String calcuCompatibitily(@RequestParam(required = true) String fname,
            @RequestParam(required = true) String sname, Model model) throws IOException {

        LoveResult rr = new LoveResult(fname, sname);

        Optional<LoveResult> r = lcSvr.getResult(rr);

        model.addAttribute("result", r.get());
        return "result";
    }

    @GetMapping(path = "/list")
    public String getAllLoveCompat(Model model) throws IOException {
        LoveResult[] lrArr = lcSvr.allResultList();
        model.addAttribute("arr", lrArr);

        return "list";
    }

    @GetMapping(path = "{id}")
    public String getLoveCompatById(@PathVariable String id, Model model) throws IOException {
        Optional<LoveResult> result = lcSvr.getById(id);

        model.addAttribute("result", result.get());
        return "result";
    }
}
