class Auth {
    constructor() {
        
        this.getProfile = this.getProfile.bind(this);
        this.handleAuthentication = this.handleAuthentication.bind(this);
        this.signOut = this.signOut.bind(this);
        this.isAuthenticated = this.isAuthenticated.bind(this);
    }

    handleAuthentication(response) {
        console.log('handleAuthentication ', response);
        this.idToken = response.tokenId;
        this.accessToken = response.accessToken;
        this.profile = response.profileObj;
    }

    signOut() {
        this.idToken = null;
        this.accessToken = null;
        this.profile = null;
    }

    getProfile() {
        return this.profile;
    }

    getIdToken() {
        return this.idToken;
    }

    getAccessToken() {
        return this.accessToken;
    }

    isAuthenticated() {
        return this.idToken != null;
    }
}

const AUTH_CLIENT = new Auth();

export default AUTH_CLIENT;