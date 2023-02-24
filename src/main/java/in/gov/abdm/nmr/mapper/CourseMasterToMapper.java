package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CourseTO;
import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class CourseMasterToMapper {

    public static List<MasterDataTO> courseToListToCourseMasterToList(List<CourseTO> course) {
        List<MasterDataTO> list = new ArrayList<>();
        course.forEach(c -> list.add(courseToMaster(c)));
        return list;
    }

    MasterDataTO courseToMaster(CourseTO courseTO) {
        MasterDataTO masterDataTO = new MasterDataTO();
        masterDataTO.setId(courseTO.getId().longValue());
        masterDataTO.setName(courseTO.getCourseName());
        return masterDataTO;
    }

}
