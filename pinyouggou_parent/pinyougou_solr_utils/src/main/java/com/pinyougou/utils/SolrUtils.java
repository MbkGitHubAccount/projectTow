package com.pinyougou.utils;


import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtils {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private TbItemMapper tbItemMapper;
    public  void DataInport(){
    List<TbItem>list= tbItemMapper.findAllGrounding();
        for (TbItem item : list) {
            String spec = item.getSpec();
         Map<String ,String> specMap  = JSON.parseObject(spec, Map.class);
            item.setSpecMap(specMap);
        }
    solrTemplate.saveBeans(list);
    solrTemplate.commit();
    }
}
