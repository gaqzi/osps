package se.sanitarium.osps.splitter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class SplitterController {
  @RequestMapping("/bill/split")
  public HttpEntity<Splitter> split(
      @RequestParam(value = "total") BigDecimal total,
      @RequestParam(value = "split") Integer split,
      @RequestParam(value = "extra", defaultValue = "0") Integer extra) {
    Splitter s = new Splitter(total, split, extra);
    s.add(linkTo(methodOn(SplitterController.class).split(total, split, extra)).withSelfRel());

    return new ResponseEntity<>(s, HttpStatus.OK);
  }

  @RequestMapping(path = "/bill/split/{billString}/{split}", method = RequestMethod.GET)
  public HttpEntity<Splitter> splitString(@PathVariable(value = "billString") String billString,
                                          @PathVariable(value = "split") Integer split) {
    Splitter s = Splitter.parse(billString + "/" + split);
    s.add(linkTo(methodOn(SplitterController.class).splitString(billString, split)).withSelfRel());

    return new ResponseEntity<>(s, HttpStatus.OK);
  }
}
