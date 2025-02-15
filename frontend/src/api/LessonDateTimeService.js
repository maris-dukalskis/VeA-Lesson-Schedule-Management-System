import BaseService from './BaseService';
import axios from 'axios';

class LessonDateTimeService extends BaseService {
    constructor() {
        super(process.env.REACT_APP_BACKEND_URL + '/lessondatetime');
    }

    getAllByLessonId(id) {
        return axios.get(`${this.baseUrl}/lesson/${id}`, super.getAuthHeaders());
    }

}

const lessonDateTimeServiceInstance = new LessonDateTimeService();

export default lessonDateTimeServiceInstance;