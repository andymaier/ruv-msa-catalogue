package de.predi8.catalogue.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.predi8.catalogue.model.Article;
import de.predi8.catalogue.repository.ArticleRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShopListener {
	private final ObjectMapper mapper;
	private final ArticleRepository repo;
	private final NullAwareBeanUtilsBean beanUtils;

	public ShopListener(ObjectMapper mapper, ArticleRepository repo, NullAwareBeanUtilsBean beanUtils) {
		this.mapper = mapper;
		this.repo = repo;
		this.beanUtils = beanUtils;
	}

	@KafkaListener(topics = "shop")
	public void listen(Operation op) throws Exception {
		System.out.println("op = " + op);

		if(op.getBo().equals("article") && op.getAction().equals("upsert")) {
			Article article = mapper.treeToValue(op.getObject(), Article.class);
			repo.save(article);
		}
	}
}