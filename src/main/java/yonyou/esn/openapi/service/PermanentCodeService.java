package yonyou.esn.openapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yonyou.esn.openapi.bo.PermanentCodeBo;
import yonyou.esn.openapi.mapper.PermanentCodeMapper;

@Service
public class PermanentCodeService {

    @Autowired
    private PermanentCodeMapper permanentCodeMapper;

    /**
     * 获取本地保存的永久授权码
     * @param qzId
     * @return
     */
    public PermanentCodeBo getPermanentCode(int qzId){
       return permanentCodeMapper.get(qzId);
    }

    /**
     * 存储永久授权码到本地
     * @param permanentCodeBo
     * @return
     */
    public int savePermanentCode(PermanentCodeBo permanentCodeBo){
        return permanentCodeMapper.insert(permanentCodeBo);
    }
}