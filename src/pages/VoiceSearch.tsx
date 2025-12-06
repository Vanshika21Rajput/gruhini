import React, { useState, useEffect } from 'react';
import Header from '@/components/Header';
import Footer from '@/components/Footer';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Mic, MicOff, Volume2, Search, Languages, Heart } from 'lucide-react';
import { toast } from 'sonner';
import useVoiceSearch from '@/hooks/useVoiceSearch';
import { useNavigate } from 'react-router-dom';

const VoiceSearch = () => {
  const [selectedLanguage, setSelectedLanguage] = useState('hi-IN');
  const [searchHistory, setSearchHistory] = useState<string[]>([]);
  const [currentTranscript, setCurrentTranscript] = useState('');
  const [liveTranscript, setLiveTranscript] = useState('');
  const [searchResults, setSearchResults] = useState<any[]>([]);
  const navigate = useNavigate();

  const languages = [
    { code: 'hi-IN', name: 'हिंदी (Hindi)', flag: '🇮🇳' },
    { code: 'en-US', name: 'English', flag: '🇺🇸' },
    { code: 'pa-IN', name: 'ਪੰਜਾਬੀ (Punjabi)', flag: '🇮🇳' },
    { code: 'mr-IN', name: 'मराठी (Marathi)', flag: '🇮🇳' },
    { code: 'bn-IN', name: 'বাংলা (Bengali)', flag: '🇮🇳' },
    { code: 'ta-IN', name: 'தமிழ் (Tamil)', flag: '🇮🇳' },
    { code: 'te-IN', name: 'తెలుగు (Telugu)', flag: '🇮🇳' },
    { code: 'gu-IN', name: 'ગુજરાતી (Gujarati)', flag: '🇮🇳' },
    { code: 'kn-IN', name: 'ಕನ್ನಡ (Kannada)', flag: '🇮🇳' },
    { code: 'ml-IN', name: 'മലയാളം (Malayalam)', flag: '🇮🇳' }
  ];

  const handleSearchResult = (transcript: string) => {
    setCurrentTranscript(transcript);
    setLiveTranscript('');
    setSearchHistory(prev => [transcript, ...prev.slice(0, 4)]);
    
    // Mock search results based on transcript
    const mockResults = [
      { id: 1, name: `Homemade ${transcript}`, price: '₹150', seller: 'Sunita Devi', image: 'https://source.unsplash.com/300x200/?homemade,food' },
      { id: 2, name: `Fresh ${transcript}`, price: '₹120', seller: 'Radha Kitchen', image: 'https://source.unsplash.com/300x200/?fresh,food' },
      { id: 3, name: `Traditional ${transcript}`, price: '₹180', seller: 'Meera Didi', image: 'https://source.unsplash.com/300x200/?traditional,food' }
    ];
    setSearchResults(mockResults);
    
    // Navigate to search results
    // navigate(`/explore?q=${encodeURIComponent(transcript)}`);
    toast.success(`Searching for: ${transcript}`);
  };

  const handleVoiceError = (error: string) => {
    toast.error(error);
  };

  const { isListening, isSupported, startListening } = useVoiceSearch({
    onResult: handleSearchResult,
    onError: handleVoiceError,
    lang: selectedLanguage,
  });

  // Add live transcription effect
  useEffect(() => {
    if (isListening) {
      const interval = setInterval(() => {
        const words = ['सुन रहे हैं...', 'Listening...', 'बोलिए...', 'Speak now...'];
        setLiveTranscript(words[Math.floor(Math.random() * words.length)]);
      }, 1000);
      return () => clearInterval(interval);
    } else {
      setLiveTranscript('');
    }
  }, [isListening]);
  const handleStartListening = () => {
    if (!isSupported) {
      toast.error('Voice search is not supported in your browser.');
      return;
    }
    setCurrentTranscript('');
    startListening();
  };

  const speakInstructions = () => {
    const instructions = {
      'hi-IN': 'आप अपनी आवाज़ से खाना खोज सकते हैं। माइक बटन दबाएं और बोलें।',
      'en-US': 'You can search for food using your voice. Press the microphone button and speak.',
      'pa-IN': 'ਤੁਸੀਂ ਆਪਣੀ ਆਵਾਜ਼ ਨਾਲ ਖਾਣਾ ਲੱਭ ਸਕਦੇ ਹੋ। ਮਾਈਕ ਬਟਨ ਦਬਾਓ ਅਤੇ ਬੋਲੋ।',
      'mr-IN': 'तुम्ही तुमच्या आवाजाने अन्न शोधू शकता. माइक बटण दाबा आणि बोला.',
      'bn-IN': 'আপনি আপনার কণ্ঠস্বর দিয়ে খাবার খুঁজতে পারেন। মাইক বোতাম টিপুন এবং বলুন।',
      'ta-IN': 'உங்கள் குரலைப் பயன்படுத்தி உணவைத் தேடலாம். மைக் பொத்தானை அழுத்தி பேசுங்கள்।',
      'te-IN': 'మీరు మీ వాయిస్‌తో ఆహారాన్ని వెతకవచ్చు. మైక్ బటన్ నొక్కి మాట్లాడండి।',
      'gu-IN': 'તમે તમારા અવાજથી ખોરાક શોધી શકો છો. માઇક બટન દબાવો અને બોલો।',
      'kn-IN': 'ನೀವು ನಿಮ್ಮ ಧ್ವನಿಯಿಂದ ಆಹಾರವನ್ನು ಹುಡುಕಬಹುದು. ಮೈಕ್ ಬಟನ್ ಒತ್ತಿ ಮಾತನಾಡಿ।',
      'ml-IN': 'നിങ്ങളുടെ ശബ്ദം ഉപയോഗിച്ച് ഭക്ഷണം തിരയാം. മൈക്ക് ബട്ടൺ അമർത്തി സംസാരിക്കുക।'
    };

    const msg = new SpeechSynthesisUtterance(instructions[selectedLanguage as keyof typeof instructions] || instructions['en-US']);
    msg.lang = selectedLanguage;
    speechSynthesis.speak(msg);
  };

  const commonSearches = [
    { text: 'दाल चावल', lang: 'hi-IN' },
    { text: 'homemade pickle', lang: 'en-US' },
    { text: 'गुलाब जामुन', lang: 'hi-IN' },
    { text: 'fresh vegetables', lang: 'en-US' },
    { text: 'हस्तनिर्मित साबुन', lang: 'hi-IN' }
  ];

  return (
    <div className="min-h-screen bg-background">
      <Header />
      
      <div className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="text-center mb-12">
          <Badge variant="outline" className="bg-purple-100 text-purple-700 border-purple-300 px-4 py-2 mb-4">
            🎤 Voice Powered
          </Badge>
          <h1 className="font-heading text-4xl md:text-5xl font-bold text-ethnic-primary mb-4">
            Voice Search
          </h1>
          <p className="text-xl text-muted-foreground max-w-3xl mx-auto">
            Search for your favorite homemade products using your voice in your preferred language. 
            Perfect for elderly users and hands-free shopping!
          </p>
        </div>

        <div className="grid lg:grid-cols-2 gap-8 max-w-6xl mx-auto">
          {/* Voice Search Interface */}
          <div className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Mic className="h-5 w-5 text-primary" />
                  Voice Search
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-6">
                {/* Language Selection */}
                <div className="space-y-2">
                  <label className="text-sm font-medium">Select Language</label>
                  <Select value={selectedLanguage} onValueChange={setSelectedLanguage}>
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {languages.map((lang) => (
                        <SelectItem key={lang.code} value={lang.code}>
                          <span className="flex items-center gap-2">
                            <span>{lang.flag}</span>
                            <span>{lang.name}</span>
                          </span>
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>

                {/* Voice Search Button */}
                <div className="text-center space-y-4">
                  <div className="relative">
                    <Button
                      onClick={handleStartListening}
                      disabled={!isSupported}
                      className={`w-32 h-32 rounded-full text-2xl transition-all duration-300 ${
                        isListening 
                          ? 'bg-destructive hover:bg-destructive/90 animate-pulse scale-110' 
                          : 'bg-primary hover:bg-primary/90'
                      }`}
                    >
                      {isListening ? (
                        <MicOff className="h-12 w-12" />
                      ) : (
                        <Mic className="h-12 w-12" />
                      )}
                    </Button>
                    
                    {isListening && (
                      <div className="absolute inset-0 rounded-full border-4 border-destructive animate-ping"></div>
                    )}
                  </div>

                  <div className="space-y-2">
                    <p className="text-lg font-semibold text-ethnic-primary">
                      {isListening ? 'Listening...' : 'Tap to Start Voice Search'}
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {isListening 
                        ? `Speaking in ${languages.find(l => l.code === selectedLanguage)?.name}` 
                        : 'Press and speak your search query'
                      }
                    </p>
                    
                    {/* Live Transcription */}
                    {isListening && (
                      <div className="p-3 bg-primary/10 rounded-lg border-2 border-primary/20">
                        <p className="text-xs text-muted-foreground mb-1">Live transcription:</p>
                        <p className="font-mono text-primary animate-pulse">
                          {liveTranscript || "Listening for your voice..."}
                        </p>
                      </div>
                    )}
                  </div>

                  {currentTranscript && (
                    <div className="p-4 bg-accent/20 rounded-lg">
                      <p className="text-sm text-muted-foreground mb-1">You said:</p>
                      <p className="font-semibold text-ethnic-primary">"{currentTranscript}"</p>
                    </div>
                  )}
                </div>

                {/* Search Results */}
                {searchResults.length > 0 && (
                  <div className="mt-6">
                    <h3 className="font-semibold text-ethnic-primary mb-3">Search Results:</h3>
                    <div className="space-y-3">
                      {searchResults.map((result) => (
                        <div key={result.id} className="flex items-center gap-3 p-3 border rounded-lg hover:bg-accent/50 transition-colors cursor-pointer">
                          <img src={result.image} alt={result.name} className="w-16 h-16 rounded-lg object-cover" />
                          <div className="flex-1">
                            <h4 className="font-medium text-ethnic-primary">{result.name}</h4>
                            <p className="text-sm text-muted-foreground">by {result.seller}</p>
                            <p className="text-lg font-bold text-trust-green">{result.price}</p>
                          </div>
                          <Button size="sm" variant="ethnic">Add to Cart</Button>
                        </div>
                      ))}
                    </div>
                  </div>
                )}
                {/* Instructions Button */}
                <div className="text-center">
                  <Button variant="outline" onClick={speakInstructions} className="flex items-center gap-2">
                    <Volume2 className="h-4 w-4" />
                    Hear Instructions
                  </Button>
                </div>

                {!isSupported && (
                  <div className="p-4 bg-destructive/10 border border-destructive/20 rounded-lg">
                    <p className="text-sm text-destructive">
                      Voice search is not supported in your browser. Please try using Chrome, Firefox, or Safari.
                    </p>
                  </div>
                )}
              </CardContent>
            </Card>

            {/* Common Searches */}
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Quick Voice Searches</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-2">
                  <p className="text-sm text-muted-foreground mb-3">Try saying these popular searches:</p>
                  {commonSearches.map((search, index) => (
                    <Button
                      key={index}
                      variant="outline"
                      size="sm"
                      className="mr-2 mb-2"
                      onClick={() => handleSearchResult(search.text)}
                    >
                      <Search className="h-3 w-3 mr-1" />
                      "{search.text}"
                    </Button>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Search History & Tips */}
          <div className="space-y-6">
            {/* Search History */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Heart className="h-5 w-5 text-primary" />
                  Recent Voice Searches
                </CardTitle>
              </CardHeader>
              <CardContent>
                {searchHistory.length === 0 ? (
                  <p className="text-muted-foreground text-center py-4">
                    Your recent voice searches will appear here
                  </p>
                ) : (
                  <div className="space-y-2">
                    {searchHistory.map((search, index) => (
                      <div
                        key={index}
                        className="flex items-center justify-between p-3 border rounded-lg hover:bg-accent/50 transition-colors cursor-pointer"
                        onClick={() => navigate(`/explore?q=${encodeURIComponent(search)}`)}
                      >
                        <span className="font-medium">"{search}"</span>
                        <Search className="h-4 w-4 text-muted-foreground" />
                      </div>
                    ))}
                  </div>
                )}
              </CardContent>
            </Card>

            {/* Tips for Better Voice Search */}
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Tips for Better Results</CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="space-y-3 text-sm">
                  <div className="flex gap-3">
                    <span className="text-primary">🎯</span>
                    <div>
                      <div className="font-medium">Speak Clearly</div>
                      <div className="text-muted-foreground">Speak slowly and clearly for better recognition</div>
                    </div>
                  </div>
                  <div className="flex gap-3">
                    <span className="text-primary">🔇</span>
                    <div>
                      <div className="font-medium">Quiet Environment</div>
                      <div className="text-muted-foreground">Use in a quiet place for best results</div>
                    </div>
                  </div>
                  <div className="flex gap-3">
                    <span className="text-primary">🗣️</span>
                    <div>
                      <div className="font-medium">Natural Speech</div>
                      <div className="text-muted-foreground">Speak naturally, don't over-pronounce</div>
                    </div>
                  </div>
                  <div className="flex gap-3">
                    <span className="text-primary">🎤</span>
                    <div>
                      <div className="font-medium">Allow Microphone</div>
                      <div className="text-muted-foreground">Make sure to allow microphone access</div>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Supported Languages */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Languages className="h-5 w-5 text-primary" />
                  Supported Languages
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-2 gap-2 text-sm">
                  {languages.slice(0, 6).map((lang) => (
                    <div key={lang.code} className="flex items-center gap-2">
                      <span>{lang.flag}</span>
                      <span>{lang.name.split(' ')[0]}</span>
                    </div>
                  ))}
                </div>
                <p className="text-xs text-muted-foreground mt-3">
                  And {languages.length - 6} more regional languages supported
                </p>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
      
      <Footer />
    </div>
  );
};

export default VoiceSearch;