package com.pinyougou.search.listener;

import GroupEntity.Goods;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.PageService;
import com.pinyougou.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemPageMessageListener implements MessageListener {

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;
    @Autowired
    private PageService pageService;
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage= (TextMessage) message;
            String goodsId  = textMessage.getText();

            Configuration configuration = freeMarkerConfig.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //需要根据Id 去查询许响应的商品
            Goods goods = pageService.findOne(Long.parseLong(goodsId));
            List<TbItem> itemList = goods.getItemList();
            for (TbItem item : itemList) {
                Map<String ,Object> map=new HashMap<>();//存放商品列表
                map.put("goods",goods);
                map.put("item",item);
                //输出到本地
                Writer writer = new FileWriter("E:\\page\\"+item.getId()+".html");
                template.process(map,writer);
                //第6步：关闭流
                writer.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
