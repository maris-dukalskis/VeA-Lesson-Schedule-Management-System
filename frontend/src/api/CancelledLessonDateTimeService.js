import BaseService from './BaseService';

class CancelledLessonDateTimeService extends BaseService {
    constructor() {
        super(`${process.env.REACT_APP_BACKEND_URL}/cancelledlessondatetime`);
    }

}

const cancelledLessonDateTimeServiceInstance = new CancelledLessonDateTimeService();

export default cancelledLessonDateTimeServiceInstance;