package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lv.venta.model.CancelledLessonDateTime;
import lv.venta.repo.ICancelledLessonDateTimeRepo;
import lv.venta.service.ICancelledLessonDateTimeService;

@Service
public class CancelledLessonDateTimeServiceImpl implements ICancelledLessonDateTimeService {

	private final ICancelledLessonDateTimeRepo cancelledLessonDateTimeRepo;

	public CancelledLessonDateTimeServiceImpl(ICancelledLessonDateTimeRepo cancelledLessonDateTimeRepo) {
		this.cancelledLessonDateTimeRepo = cancelledLessonDateTimeRepo;
	}

	@Override
	public ArrayList<CancelledLessonDateTime> selectAllCancelledLessonDateTimes() throws Exception {
		if (cancelledLessonDateTimeRepo.count() == 0)
			return new ArrayList<CancelledLessonDateTime>();
		return (ArrayList<CancelledLessonDateTime>) cancelledLessonDateTimeRepo.findAll();
	}

	@Override
	public CancelledLessonDateTime selectCancelledLessonDateTimeById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!cancelledLessonDateTimeRepo.existsById(id)) {
			throw new Exception("CancelledLessonDateTime by that ID does not exist");
		}
		return cancelledLessonDateTimeRepo.findById(id).get();
	}

	@Override
	public void deleteCancelledLessonDateTimeById(int id) throws Exception {
		cancelledLessonDateTimeRepo.delete(selectCancelledLessonDateTimeById(id));
	}

	@Override
	public CancelledLessonDateTime insertNewCancelledLessonDateTime(CancelledLessonDateTime cancelledLessonDateTime)
			throws Exception {
		if (cancelledLessonDateTime == null)
			throw new Exception("CancelledLessonDateTime object cannot be null");
		List<CancelledLessonDateTime> cancelledLessonDateTimes = new ArrayList<>();
		try {
			cancelledLessonDateTimes = selectAllCancelledLessonDateTimes();
		} catch (Exception e) {
		}
		if (!cancelledLessonDateTimes.isEmpty()) {
			for (CancelledLessonDateTime dbCancelledLessonDateTime : cancelledLessonDateTimes) {
				if (dbCancelledLessonDateTime.getCancelledLessonDateTimeId() == cancelledLessonDateTime
						.getCancelledLessonDateTimeId()) {
					throw new Exception("CancelledLessonDateTime already exists");
				}
			}
		}
		return cancelledLessonDateTimeRepo.save(cancelledLessonDateTime);
	}

	@Override
	public CancelledLessonDateTime updateCancelledLessonDateTimeById(int id,
			CancelledLessonDateTime cancelledLessonDateTime) throws Exception {
		CancelledLessonDateTime oldCancelledLessonDateTime = selectCancelledLessonDateTimeById(id);
		oldCancelledLessonDateTime.setDate(cancelledLessonDateTime.getDate());
		oldCancelledLessonDateTime.setLessonDateTime(cancelledLessonDateTime.getLessonDateTime());
		oldCancelledLessonDateTime.setReason(cancelledLessonDateTime.getReason());
		oldCancelledLessonDateTime.setRescheduled(cancelledLessonDateTime.isRescheduled());
		cancelledLessonDateTimeRepo.save(oldCancelledLessonDateTime);
		return oldCancelledLessonDateTime;
	}

	@Override
	public ArrayList<CancelledLessonDateTime> selectByLessonDateTimeId(int id) throws Exception {
		if (cancelledLessonDateTimeRepo.count() == 0)
			return new ArrayList<CancelledLessonDateTime>();
		return (ArrayList<CancelledLessonDateTime>) cancelledLessonDateTimeRepo
				.findByLessonDateTimeLessonDateTimeId(id);
	}

}
