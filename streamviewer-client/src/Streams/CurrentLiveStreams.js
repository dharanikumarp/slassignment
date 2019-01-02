class CurrentLiveStreams {

    constructor() {
        this.setStreams = this.setStreams.bind(this);
    }

    setStreams(streams) {
        this.streams = streams;
    }

    getStream(streamId) {
        let stream = null;
        if(this.streams) {
            stream = this.streams.find(e => e.videoId === streamId);
        }
        return stream;
    }
}

const CURR_LIVE_STREAMS = new CurrentLiveStreams();

export default CURR_LIVE_STREAMS;