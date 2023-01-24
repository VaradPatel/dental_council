package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.entity.WorkFlowAudit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IWorkFlowMasterMapper {

	List<WorkFlowAudit> workflowToMaster(List<WorkFlow> workFlows);

}
