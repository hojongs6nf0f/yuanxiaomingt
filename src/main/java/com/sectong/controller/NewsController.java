package com.sectong.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sectong.domain.News;
import com.sectong.domain.NewsCreateForm;
import com.sectong.message.Message;
import com.sectong.service.NewsService;

@RestController
@RequestMapping(value = "/api/v1", name = "新闻API")
public class NewsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);
	private Message message = new Message();

	private NewsService newsService;

	@Autowired
	public NewsController(NewsService newsService) {
		this.newsService = newsService;
	}

	@ResponseBody
	@RequestMapping(value = "i/news/create", method = RequestMethod.POST)
	public ResponseEntity<Message> createNews(@Valid @RequestBody NewsCreateForm form, BindingResult bindingResult) {
		try {
			News news = newsService.create(form);
			// ok, redirect
			message.setMsg(1, "新闻创建成功", news);
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			LOGGER.warn("create news error", e);
			message.setMsg(0, "创建新闻失败");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "news/getNewsList", method = RequestMethod.GET)
	public ResponseEntity<Message> getNewsList(Pageable p) {
//		Sort sort = new Sort(Direction.DESC, "id");
//		Pageable p = new PageRequest(page, size, sort);
		Page<News> news = newsService.getNewsList(p);
		message.setMsg(1, "获取新闻列表成功", news);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

}