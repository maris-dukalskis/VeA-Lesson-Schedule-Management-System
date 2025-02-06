import BaseService from './BaseService';

class UserService extends BaseService {
    constructor() {
        super(process.env.REACT_APP_BACKEND_URL + '/user');
    }

}

const userServiceInstance = new UserService();

export default userServiceInstance;