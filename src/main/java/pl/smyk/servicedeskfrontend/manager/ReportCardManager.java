package pl.smyk.servicedeskfrontend.manager;

import pl.smyk.servicedeskfrontend.dto.ReportDto;

import java.util.List;

public class ReportCardManager {
  private static ReportCardManager instance;
  private List<ReportDto> reportDtoList;

  private ReportCardManager() {}

  public static ReportCardManager getInstance() {
    if (instance == null) {
      instance = new ReportCardManager();
    }
    return instance;
  }

  public List<ReportDto> getReportDtoList() {
    return reportDtoList;
  }

  public void setReportDtoList(List<ReportDto> reportDtoList) {
    this.reportDtoList = reportDtoList;
  }


}
