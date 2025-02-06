import BaseService from './BaseService';

class StudentService extends BaseService {
    constructor() {
        super(process.env.REACT_APP_BACKEND_URL + '/student');
    }

}

const studentServiceInstance = new StudentService();

export default studentServiceInstance;