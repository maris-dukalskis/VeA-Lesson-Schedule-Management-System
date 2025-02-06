import BaseService from './BaseService';

class LecturerService extends BaseService {
    constructor() {
        super(process.env.REACT_APP_BACKEND_URL + '/lecturer');
    }

}

const lecturerServiceInstance = new LecturerService();

export default lecturerServiceInstance;