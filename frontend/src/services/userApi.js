import API from './api'; 

export const updateProfile = async (data) => {
    try {
        const response = await API.put('/auth/profile', data);
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const updatePassword = async (data) => {
    try {
        const response = await API.put('/auth/password', data);
        return response.data;
    } catch (error) {
        throw error;
    }
};
