import BaseService from './BaseService';
import axios from 'axios';

class CourseStudyProgrammeAliasService extends BaseService {
    constructor() {
        super(`${process.env.REACT_APP_BACKEND_URL}/coursestudyprogrammealias`);
    }

    getAllByCourseId(id) {
        return axios.get(`${this.baseUrl}/course/${id}`, super.getAuthHeaders());
    }

}

const courseStudyProgrammeAliasServiceInstance = new CourseStudyProgrammeAliasService();

export default courseStudyProgrammeAliasServiceInstance;