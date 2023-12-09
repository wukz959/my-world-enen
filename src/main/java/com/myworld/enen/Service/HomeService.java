package com.myworld.enen.Service;

import com.myworld.enen.bean.Barrage;
import com.myworld.enen.dto.BarrageDto;
import com.myworld.enen.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName HomeService
 * @Author wkz
 * @Date 2023/12/4 9:01
 * @Version 1.0
 */
@Service
public class HomeService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<BarrageDto> getBarrage() {
        Criteria criteria = Criteria.where("hadDeleted").is(false);
        List<Barrage> barrages = mongoTemplate.find(Query.query(criteria), Barrage.class);
        List<BarrageDto> barrageDtoList = new ArrayList<>();
        barrages.forEach(item -> {
            BarrageDto barrageDto = new BarrageDto();
            BeanUtils.copyProperties(item, barrageDto);
            barrageDtoList.add(barrageDto);
        });
        return barrageDtoList;
    }

    public void saveBarrage(Barrage barrage, HttpServletRequest request)
    {
        String remoteHost = request.getRemoteAddr();
        barrage.setPublishIp(remoteHost);
        mongoTemplate.save(barrage);
    }
}
