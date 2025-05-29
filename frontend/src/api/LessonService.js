import BaseService from './BaseService';
import axios from 'axios';

class LessonService extends BaseService {
    constructor() {
        super(`${process.env.REACT_APP_BACKEND_URL}/lesson`);
    }
    getByStudyProgrammeNameAndYear(name, year) {
        return axios.get(`${this.baseUrl}/studyprogramme/${name}/${year}`, super.getAuthHeaders());
    }

    getByLecturerName(name) {
        return axios.get(`${this.baseUrl}/lecturer/${name}`, super.getAuthHeaders());
    }

    getByClassroomBuildingAndNumber(building, number){
        return axios.get(`${this.baseUrl}/classroom/${building}/${number}`, super.getAuthHeaders());
    }
}

const lessonServiceInstance = new LessonService();

export default lessonServiceInstance;