package GroupEntity;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {


    private TbSpecification specification;

    private List<TbSpecificationOption> sppecificationOption;

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification(TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSppecificationOption() {
        return sppecificationOption;
    }

    public void setSppecificationOption(List<TbSpecificationOption> sppecificationOption) {
        this.sppecificationOption = sppecificationOption;
    }
}

