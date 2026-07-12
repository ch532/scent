// Mock Data for TV Channels (using safe YouTube embed URLs for demo)
const channels = [
    { id: 1, name: 'News Channel 1 (Live)', url: 'https://www.youtube.com/embed/5aCg9K9j1uI' }, // Demo URL
    { id: 2, name: 'Sports Highlights', url: 'https://www.youtube.com/embed/AQ6GmpMu5L8' },
    { id: 3, name: 'Nature & Discovery', url: 'https://www.youtube.com/embed/jfKfPfyJRdk' }
];

// Mock Data for Content
const newsData = [
    { title: 'Global Tech Summit 2024', summary: 'The biggest tech innovations revealed this year in Silicon Valley.' },
    { title: 'Economic Market Update', summary: 'Stock markets see a significant rise following new trade agreements.' }
];

const storiesData = [
    { title: 'Life in the Himalayas', summary: 'A breathtaking journey exploring the culture of remote mountain villages.' },
    { title: 'The Future of AI', summary: 'How artificial intelligence is reshaping creative industries.' }
];

const magazinesData = [
    { title: 'Style & Design Monthly', summary: 'Top trends in interior design for modern homes.' },
    { title: 'Culinary Delights', summary: '10 quick and healthy recipes for busy professionals.' }
];

// DOM Elements
const channelListEl = document.getElementById('channel-list');
const tvIframeEl = document.getElementById('tv-iframe');
const newsContainer = document.getElementById('news-container');
const storiesContainer = document.getElementById('stories-container');
const magazinesContainer = document.getElementById('magazines-container');

// Initialize TV Player
function initTV() {
    channels.forEach((channel, index) => {
        const btn = document.createElement('button');
        btn.className = 'channel-btn';
        if (index === 0) btn.classList.add('active'); // Set first channel as active initially
        btn.textContent = channel.name;

        btn.addEventListener('click', () => {
            // Update Iframe source
            tvIframeEl.src = channel.url;

            // Update active state styling
            document.querySelectorAll('.channel-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });

        channelListEl.appendChild(btn);
    });

    // Load first channel by default
    if (channels.length > 0) {
        tvIframeEl.src = channels[0].url;
    }
}

// Render Content Function
function renderContent(data, container) {
    data.forEach(item => {
        const div = document.createElement('div');
        div.className = 'content-item';
        div.innerHTML = `
            <h4>${item.title}</h4>
            <p>${item.summary}</p>
        `;
        container.appendChild(div);
    });
}

// Initialize Application
function initApp() {
    initTV();
    renderContent(newsData, newsContainer);
    renderContent(storiesData, storiesContainer);
    renderContent(magazinesData, magazinesContainer);
}

// Run when DOM is fully loaded
document.addEventListener('DOMContentLoaded', initApp);
