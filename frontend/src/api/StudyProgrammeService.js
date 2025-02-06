import BaseService from './BaseService';

class StudyProgrammeService extends BaseService {
    constructor() {
        super(process.env.REACT_APP_BACKEND_URL + '/studyprogramme');
    }

}

const studyProgrammeServiceInstance = new StudyProgrammeService();

export default studyProgrammeServiceInstance;