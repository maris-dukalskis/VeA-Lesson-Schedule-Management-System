import BaseService from './BaseService';

class LessonService extends BaseService {
    constructor() {
        super(process.env.REACT_APP_BACKEND_URL + '/lesson');
    }

}

const lessonServiceInstance = new LessonService();

export default lessonServiceInstance;