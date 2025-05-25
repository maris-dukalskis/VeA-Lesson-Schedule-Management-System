package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
	public List<CancelledLessonDateTime> selectAllCancelledLessonDateTimes() {
		if (cancelledLessonDateTimeRepo.count() == 0)
			return new ArrayList<>();
		return (ArrayList<CancelledLessonDateTime>) cancelledLessonDateTimeRepo.findAll();
	}

	@Override
	public CancelledLessonDateTime selectCancelledLessonDateTimeById(int id)
			throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!cancelledLessonDateTimeRepo.existsById(id)) {
			throw new NoSuchElementException("CancelledLessonDateTime by that ID does not exist");
		}
		return cancelledLessonDateTimeRepo.findById(id).get();
	}

	@Override
	public void deleteCancelledLessonDateTimeById(int id) {
		cancelledLessonDateTimeRepo.delete(selectCancelledLessonDateTimeById(id));
	}

	@Override
	public CancelledLessonDateTime insertNewCancelledLessonDateTime(CancelledLessonDateTime cancelledLessonDateTime)
			throws NullPointerException, IllegalStateException {
		if (cancelledLessonDateTime == null)
			throw new NullPointerException("CancelledLessonDateTime object cannot be null");
		List<CancelledLessonDateTime> cancelledLessonDateTimes = new ArrayList<>();

		cancelledLessonDateTimes = selectAllCancelledLessonDateTimes();

		if (!cancelledLessonDateTimes.isEmpty()) {
			for (CancelledLessonDateTime dbCancelledLessonDateTime : cancelledLessonDateTimes) {
				if (dbCancelledLessonDateTime.getCancelledLessonDateTimeId() == cancelledLessonDateTime
						.getCancelledLessonDateTimeId()) {
					throw new IllegalStateException("CancelledLessonDateTime already exists");
				}
			}
		}
		return cancelledLessonDateTimeRepo.save(cancelledLessonDateTime);
	}

	@Override
	public CancelledLessonDateTime updateCancelledLessonDateTimeById(int id,
			CancelledLessonDateTime cancelledLessonDateTime) {
		CancelledLessonDateTime oldCancelledLessonDateTime = selectCancelledLessonDateTimeById(id);
		oldCancelledLessonDateTime.setDate(cancelledLessonDateTime.getDate());
		oldCancelledLessonDateTime.setLessonDateTime(cancelledLessonDateTime.getLessonDateTime());
		oldCancelledLessonDateTime.setReason(cancelledLessonDateTime.getReason());
		oldCancelledLessonDateTime.setRescheduled(cancelledLessonDateTime.isRescheduled());
		cancelledLessonDateTimeRepo.save(oldCancelledLessonDateTime);
		return oldCancelledLessonDateTime;
	}

	@Override
	public List<CancelledLessonDateTime> selectByLessonDateTimeId(int id) {
		if (cancelledLessonDateTimeRepo.count() == 0)
			return new ArrayList<>();
		return cancelledLessonDateTimeRepo.findByLessonDateTimeLessonDateTimeId(id);
	}

}
