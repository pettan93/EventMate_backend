package gr.tei.erasmus.pp.eventmate.backend.repository;

import gr.tei.erasmus.pp.eventmate.backend.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


}
