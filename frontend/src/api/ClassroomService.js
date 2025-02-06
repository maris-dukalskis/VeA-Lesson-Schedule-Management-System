import BaseService from './BaseService';

class ClassroomService extends BaseService {
    constructor() {
        super(process.env.REACT_APP_BACKEND_URL + '/classroom');
    }

}

const classroomServiceInstance = new ClassroomService();

export default classroomServiceInstance;