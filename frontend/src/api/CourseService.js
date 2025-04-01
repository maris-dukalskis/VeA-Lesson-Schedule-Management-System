import BaseService from './BaseService';

class CourseService extends BaseService {
    constructor() {
        super(`${process.env.REACT_APP_BACKEND_URL}/course`);
    }

}

const courseServiceInstance = new CourseService();

export default courseServiceInstance;