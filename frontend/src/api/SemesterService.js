import BaseService from './BaseService';

class SemesterService extends BaseService {
    constructor() {
        super(`${process.env.REACT_APP_BACKEND_URL}/semester`);
    }

}

const semesterServiceInstance = new SemesterService();

export default semesterServiceInstance;