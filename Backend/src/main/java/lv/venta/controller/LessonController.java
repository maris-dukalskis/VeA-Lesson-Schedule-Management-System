package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lv.venta.service.ILessonService;

@RestController
@RequestMapping("/lesson")
public class LessonController {

	@Autowired
	ILessonService lessonService;

}
