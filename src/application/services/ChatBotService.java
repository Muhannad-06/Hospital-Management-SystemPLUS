package application.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ChatBotService {

    private static final Map<List<String>, String[]> responses = new HashMap<>();

    static {
    	
    	// AI involved in the Medical Statements 
        // Greetings
        responses.put(Arrays.asList("hello", "hi", "hey", "greetings", "good morning", "good evening", "welcome", "السلام", "عليكم", "السلام عليكم"),
            new String[]{"Peace be upon you! 🌟 Welcome to our hospital. How can I help you today?",
                         "Welcome! I am your smart medical assistant. How may I serve you?",
                         "Hello! We are honored by your visit. Are you experiencing any symptoms you'd like to ask about?"});

        // Well-being
        responses.put(Arrays.asList("how are you", "how do you do", "what's up", "how are things"),
            new String[]{"I am doing well, thank you for asking! How are you feeling today?",
                         "Well and energetic! Are you suffering from any pain or discomfort?"});

        // Symptoms - Head
        responses.put(Arrays.asList("headache", "headaches", "migraine", "head hurts", "head pain"),
            new String[]{"Headaches have many causes: stress, lack of sleep, dehydration, or vision problems. My advice: drink plenty of water and rest a bit. If the headache persists, it is advisable to consult a neurologist.",
                         "To treat a headache: rest in a dark room, drink water, and avoid stress. You can use a pain reliever like Paracetamol. If the headache is severe or frequent, please visit a doctor."});

        // Symptoms - Fever
        responses.put(Arrays.asList("fever", "temperature", "hot", "feverish", "high temp", "حرارة", "حراره"),
            new String[]{"A fever (high temperature) is usually a normal response to infection. If your temperature is below 38.5°C, drink plenty of fluids and rest. If it rises above 39°C or lasts more than two days, please visit a doctor immediately.",
                         "To lower a fever: apply cold compresses to your forehead, drink warm fluids, and get plenty of rest. You can use a fever reducer like Paracetamol."});

        // Symptoms - Respiratory
        responses.put(Arrays.asList("cough", "coughing", "covid", "corona", "sore throat", "انفلونزا"),
            new String[]{"A cough can be caused by a cold, allergies, or an infection. For relief: drink warm beverages like anise and ginger, and use honey with lemon. If the cough lasts more than two weeks, please consult a pulmonologist.",
                         "To treat a cough: drink warm liquids, consume honey, and rest your vocal cords. You may also use an expectorant medication if needed."});

        // Symptoms - Stomach
        responses.put(Arrays.asList("stomach", "stomachache", "belly", "abdomen", "gastric", "nausea", "tummy ache"),
            new String[]{"Abdominal pain may result from indigestion, gastritis, or colon issues. My advice: eat light meals and avoid fatty or spicy foods. If the pain is severe or accompanied by blood, go to the emergency room immediately.",
                         "To soothe your stomach: drink peppermint or chamomile tea, and avoid heavy foods. If the pain persists, please visit a gastroenterologist."});

        // Chronic Conditions - Diabetes
        responses.put(Arrays.asList("diabetes", "diabetic", "blood sugar", "insulin"),
            new String[]{"Diabetes is an elevation of glucose levels in the blood. There are two types: Type 1 (requires insulin) and Type 2 (managed with medication and diet). My advice: have regular follow-ups, monitor your blood sugar frequently, and follow a balanced diet.",
                         "For diabetic patients: eat small, frequent meals, choose foods with a low glycemic index, and exercise moderately. Visit a diabetes clinic for regular check-ups."});

        // Chronic Conditions - Hypertension
        responses.put(Arrays.asList("blood pressure", "hypertension", "high blood pressure", "pressure"),
            new String[]{"High blood pressure is often called the 'silent killer.' It is crucial to monitor it regularly. My advice: reduce salt intake, quit smoking, exercise, and avoid stress. It is recommended to visit a cardiologist for a follow-up.",
                         "To help regulate blood pressure: follow a low-sodium diet, ensure you get enough sleep, and avoid caffeine and smoking."});

        // Clinics - Cardiology
        responses.put(Arrays.asList("heart", "cardio", "chest pain", "heartbeat"),
            new String[]{"For heart health: exercise regularly, eat a healthy diet rich in fiber and Omega-3 fatty acids, and avoid smoking and caffeine. We have a specialized cardiology clinic where you can book an appointment.",
                         "Warning signs of a heart issue: chest pain, shortness of breath, and dizziness. If you experience these symptoms, head to the emergency room immediately!"});

        // Clinics - Neurology
        responses.put(Arrays.asList("neuro", "neurology", "nerve", "nerves", "brain", "numbness"),
            new String[]{"The neurology clinic treats: chronic headaches, migraines, back and neck pain, numbness and tingling, and sleep disorders. You can book an appointment with our neurologist.",
                         "For nerve health: get adequate sleep, eat foods rich in Vitamin B, and engage in mental exercises like reading."});

        // Clinics - Orthopedics
        responses.put(Arrays.asList("ortho", "orthopedic", "bone", "bones", "joint", "joints", "back pain", "spine"),
            new String[]{"The orthopedics and joints clinic treats: back and neck pain, arthritis, osteoporosis, and fractures. You can book an appointment for a thorough examination.",
                         "For bone health: consume foods rich in calcium and Vitamin D (milk, cheese, yogurt, fish), and incorporate regular walks into your routine."});

        // Clinics - Oncology
        responses.put(Arrays.asList("cancer", "oncology", "tumor", "tumors", "chemo"),
            new String[]{"The oncology clinic provides: early cancer screening, chemotherapy and radiotherapy, and regular follow-ups. Early detection significantly increases the cure rate.",
                         "Cancer prevention: quitting smoking, eating plenty of fruits and vegetables, exercising regularly, and scheduling periodic check-ups."});

        // System - Appointments
        responses.put(Arrays.asList("appointment", "book", "booking", "schedule", "reservation", "حجز", "موعد", "استشاره", "كشف"),
            new String[]{"To book an appointment: select one of the available clinics (Cardiology, Neurology, Orthopedics, Oncology) and click the appropriate booking button on the dashboard.",
                         "To view your appointments: look at the upcoming appointments section on your dashboard. If you need to reschedule, please contact the reception desk."});

        // Polite Closings
        responses.put(Arrays.asList("thank", "thanks", "thx", "appreciate", "شكرا", "الشكر"),
            new String[]{"Thank you too! We are always at your service. Do you need anything else?",
                         "You're very welcome! I wish you lasting health and wellness."});

        responses.put(Arrays.asList("bye", "goodbye", "see you", "farewell"),
            new String[]{"Goodbye! We wish you a speedy recovery. Feel free to contact us whenever you need assistance.",
                         "Take care and stay safe! Goodbye."});

        // System - Help
        responses.put(Arrays.asList("help", "options", "what can you do", "commands", "assist"),
            new String[]{"I am your smart medical assistant. I can:\n1️⃣ Answer medical questions (headaches, fever, cough, etc.)\n2️⃣ Explain diseases and health conditions\n3️⃣ Provide information about our clinics (Cardiology, Neurology, Orthopedics, Oncology)\n4️⃣ Give health and preventive tips\n5️⃣ Assist with appointments and bookings",
                         "Available services:\n• Ask about any medical symptom\n• Learn more about our clinics\n• Get general health tips"});

        // Clinics Info
        responses.put(Arrays.asList("clinic", "clinics", "specialty", "specialties", "departments"),
            new String[]{"Our hospital clinics:\n1️⃣ Cardiology Clinic\n2️⃣ Neurology Clinic\n3️⃣ Orthopedics Clinic\n4️⃣ Oncology Clinic\nTo book an appointment, please select the respective clinic from your dashboard."});

        // Health Tips
        responses.put(Arrays.asList("tip", "tips", "health tip", "advice", "suggestion"),
            new String[]{"Health tip: Drink at least 8 cups of water daily, walk for 30 minutes every day, and eat 5 servings of fruits and vegetables.",
                         "For healthy sleep: Aim for 7-8 hours of sleep, avoid using screens before bed, and keep your bedroom dark and quiet."});

        // Asthma
        responses.put(Arrays.asList("asthma", "breathing", "respiratory", "shortness of breath", "wheezing"),
            new String[]{"Asthma is a narrowing of the airways that leads to difficulty breathing. Common triggers include dust, pollen, smoking, and stress. It is usually treated with bronchodilator inhalers and antihistamines."});

        // Allergies
        responses.put(Arrays.asList("allergy", "allergies", "allergic", "rash", "sneezing"),
            new String[]{"An allergy is an immune system reaction to a specific trigger. Symptoms can include sneezing, a runny nose, itchy eyes, and skin rashes. My advice: avoid known triggers and consult a doctor to prescribe antihistamines."});

        // Surgery
        responses.put(Arrays.asList("surgery", "surgical", "operation"),
            new String[]{"For various surgical needs, we have specialized surgical teams in cardiology, orthopedics, and oncology. Please consult with a specialist for further details and evaluation."});

        // Vitamins
        responses.put(Arrays.asList("vitamin", "vitamins", "supplement", "supplements"),
            new String[]{"Vitamins are essential for your overall health. Vitamin D supports bones, Vitamin C boosts immunity, and Vitamin B supports the nervous system. It is always best to obtain them from natural food sources."});

        // Hospital Info
        responses.put(Arrays.asList("reception", "hospital", "hours", "opening hours", "info"),
            new String[]{"Our hospital clinics operate from 8:00 AM to 8:00 PM, 7 days a week. The Emergency Department is open 24/7. Please visit the reception desk for any administrative inquiries."});

        // Emergency
        responses.put(Arrays.asList("emergency", "ambulance", "urgent", "123"),
            new String[]{"In case of emergencies: call 114 or proceed directly to the Emergency Department immediately! Do not hesitate to ask for urgent help.",
                         "Our Emergency Department is open 24 hours a day, 7 days a week."});

        // High Fever
        responses.put(Arrays.asList("high fever", "severe fever", "burning up"),
            new String[]{"A high fever (over 39°C) requires immediate medical attention. Please head to the emergency room or consult a doctor right away."});

        // Insomnia
        responses.put(Arrays.asList("insomnia", "sleep", "can't sleep", "sleepless"),
            new String[]{"Insomnia is difficulty falling or staying asleep. Tips: avoid caffeine before bedtime, maintain a consistent sleep schedule, and ensure your bedroom environment is dark, cool, and quiet."});

        // Labs
        responses.put(Arrays.asList("checkup", "test", "lab", "laboratory", "analysis", "blood test"),
            new String[]{"Routine check-ups are vital for early detection of health issues. We have a fully equipped laboratory for all tests: blood, urine, and chemical analyses. Please check with reception to book a lab appointment."});

        // Medicine
        responses.put(Arrays.asList("medicine", "medication", "drug", "drugs", "prescription", "pharmacy"),
            new String[]{"Please do not take any medication without consulting a doctor first. We have an in-house pharmacy that can dispense your prescribed medications."});

        // Diet
        responses.put(Arrays.asList("diet", "nutrition", "food", "eating", "weight loss"),
            new String[]{"For a healthy diet: eat a diverse range of foods, choose whole grains and healthy proteins, and increase your daily intake of fresh vegetables and fruits."});

        // Exercise
        responses.put(Arrays.asList("sport", "sports", "exercise", "workout", "fitness"),
            new String[]{"Physical activity is key: try walking for 30 minutes daily, practicing stretching exercises, or swimming. Always consult your doctor before starting any new exercise program."});

        // Trauma / Bleeding
        responses.put(Arrays.asList("bleed", "bleeding", "blood", "cut", "wound", "hemorrhage"),
            new String[]{"In case of active bleeding: apply firm pressure to the wound with a clean cloth, elevate the injured area above the heart if possible, and go to the emergency room immediately."});

        // Prayers / Well wishes
        responses.put(Arrays.asList("pray", "prayer", "heal", "healing", "bless"),
            new String[]{"Amen! I pray to God for your speedy recovery and lasting, good health. 🤲"});
        
        // Azhkar
        responses.put(Arrays.asList("اذكار", "دعاء", "الاذكار", "الدعاء", "ذكر"),
        	new String[]{"بِسْمِ اللهِ الرَّحْمنِ الرَّحِيم\n"
        			+ "اللَّهُ لاَ إِلَهَ إِلاَّ هُوَ الْحَيُّ الْقَيُّومُ لاَ تَأْخُذُهُ سِنَةٌ وَلاَ نَوْمٌ لَّهُ مَا فِي السَّمَوَاتِ وَمَا فِي الأَرْضِ مَن ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلاَّ بِإِذْنِهِ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ وَلاَ يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلاَّ بِمَا شَاء وَسِعَ كُرْسِيُّهُ السَّمَوَاتِ وَالأَرْضَ وَلاَ يَؤُودُهُ حِفْظُهُمَا وَهُوَ الْعَلِيُّ الْعَظِيمُ"
        			+ "\n\n\n"
        			+"بِسْمِ اللهِ الرَّحْمنِ الرَّحِيم\n"
        			+ "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ كُلٌّ آمَنَ بِاللَّهِ وَمَلآئِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لاَ نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ وَقَالُواْ سَمِعْنَا وَأَطَعْنَا غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ* لاَ يُكَلِّفُ اللَّهُ نَفْساً إِلاَّ وُسْعَهَا لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ رَبَّنَا لاَ تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا رَبَّنَا وَلاَ تَحْمِلْ عَلَيْنَا إِصْراً كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِن قَبْلِنَا رَبَّنَا وَلاَ تُحَمِّلْنَا مَا لاَ طَاقَةَ لَنَا بِهِ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَآ أَنتَ مَوْلاَنَا فَانصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ"
        			+"\n\n\n"
        			+ "اللَّهُمَّ عَالِمَ الغَيْبِ وَالشَّهَادَةِ فَاطِرَ السَّمَوَاتِ وَالْأَرْضِ، رَبَّ كُلِّ شَيْءٍ وَمَلِيكَهُ، أَشْهَدُ أَنْ لاَ إِلَهَ إِلاَّ أَنْتَ، أَعُوذُ بِكَ مِنْ شَرِّ نَفْسِي، وَمِنْ شَرِّ الشَّيْطانِ وَشِرْكِهِ، وَأَنْ أَقْتَرِفَ عَلَى نَفْسِي سُوءاً، أَوْ أَجُرَّهُ إِلَى مُسْلِمٍ.\n"});
       responses.put(Arrays.asList("الشفاء", "علاج", "treatment", "Duaa", "شفاء", "العلاج") ,
    		new String[]{"آيات الشفاء في القرآن الكريم ست، هي:\n"
    				+ "\n"
    				+ "1 : قوله تعالى: قَاتِلُوهُمْ يُعَذِّبْهُمُ اللَّهُ بِأَيْدِيكُمْ وَيُخْزِهِمْ وَيَنْصُرْكُمْ عَلَيْهِمْ وَيَشْفِ صُدُورَ قَوْمٍ مُؤْمِنِينَ {التوبة:14}.\n"
    				+ "\n"
    				+ "2. قوله تعالى: يَا أَيُّهَا النَّاسُ قَدْ جَاءَتْكُمْ مَوْعِظَةٌ مِنْ رَبِّكُمْ وَشِفَاءٌ لِمَا فِي الصُّدُورِ وَهُدًى وَرَحْمَةٌ لِلْمُؤْمِنِينَ {يونس:57}.\n"
    				+ "\n"
    				+ "3. قوله تعالى: ثُمَّ كُلِي مِنْ كُلِّ الثَّمَرَاتِ فَاسْلُكِي سُبُلَ رَبِّكِ ذُلُلًا يَخْرُجُ مِنْ بُطُونِهَا شَرَابٌ مُخْتَلِفٌ أَلْوَانُهُ فِيهِ شِفَاءٌ لِلنَّاسِ إِنَّ فِي ذَلِكَ لَآيَةً لِقَوْمٍ يَتَفَكَّرُونَ {النحل:69}.\n"
    				+ "\n"
    				+ "4. قوله تعالى: وَنُنَزِّلُ مِنَ الْقُرْآنِ مَا هُوَ شِفَاءٌ وَرَحْمَةٌ لِلْمُؤْمِنِينَ وَلَا يَزِيدُ الظَّالِمِينَ إِلَّا خَسَارًا {الإسراء:82}.\n"
    				+ "\n"
    				+ "5. قوله تعالى: وَالَّذِي هُوَ يُطْعِمُنِي وَيَسْقِينِ * وَإِذَا مَرِضْتُ فَهُوَ يَشْفِينِ {الشعراء:79-80}.\n"
    				+ "\n"
    				+ "6. قوله تعالى: وَلَوْ جَعَلْنَاهُ قُرْآنًا أَعْجَمِيًّا لَقَالُوا لَوْلَا فُصِّلَتْ آيَاتُهُ أَأَعْجَمِيٌّ وَعَرَبِيٌّ قُلْ هُوَ لِلَّذِينَ آمَنُوا هُدًى وَشِفَاءٌ وَالَّذِينَ لَا يُؤْمِنُونَ فِي آذَانِهِمْ وَقْرٌ وَهُوَ عَلَيْهِمْ عَمًى أُولَئِكَ يُنَادَوْنَ مِنْ مَكَانٍ بَعِيدٍ" });
       responses.put(Arrays.asList("خوف", "سكينه", "طمأنينه", "خائف", "خايف", "بخاف", "رعب", "مرعوب", "الخوف", "قلق", "القلق", "توتر", "متوتر", "التوتر", "قلقان"),
    		new String[] {"بسم الله الرحمن الرحيم"
    				+"\n\n"
    				+"﴿الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُمْ بِذِكْرِ اللَّهِ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ﴾."
    		   		+"\n\n"
    		   		+"توكل على الله واكثر من الصلاة علي النبي"
    		   		+"\n\n"
    		   		+"بسم الله الرحمن الرحيم"
    		   		+"\n\n"
    		   		+"إِن يَنصُرْكُمُ اللَّهُ فَلَا غَالِبَ لَكُمْ ۖ وَإِن يَخْذُلْكُمْ فَمَن ذَا الَّذِي يَنصُرُكُم مِّن بَعْدِهِ ۗ وَعَلَى اللَّهِ فَلْيَتَوَكَّلِ الْمُؤْمِنُونَ"});
       responses.put(Arrays.asList("Doctors"),
    		new String[] { "Dr.Mohamed Mahfouz    <Neurology>\n"
    					+  "Dr.Sara Khalil        <Neurology>\n"
    					+  "Dr.Moaz Ibrahim       <Cardiology>\n"
    					+  "Dr.Laila Ahmed        <Cardiology>\n"
    					+  "Dr.Khaled El-Sayed    <Orthopedics>\n"
    					+  "Dr.Nermen Fouad    	  <Orthopedics>\n"
    					+  "Dr.Yousef Emad        <Cancer Care>\n"
    					+  "Dr.Mariam Mourad      <Cancer Care>\n"
    		});
    														
    }

    private static final String[] fallbacks = {
        "Thank you for your question. I am not entirely sure of the answer. Could you rephrase your question, or ask about another medical topic? Alternatively, please consult a specialist doctor.",
        "I apologize, I didn't clearly understand your question. I can assist you with general medical queries (headaches, fever, coughs, etc.) or provide information about our clinics and how to book appointments.",
        "I'm sorry, I cannot answer this question accurately. It is highly recommended to consult a specialist doctor or type 'help' to see the topics I can assist you with.",
        "Maybe Somthing with your Spelling"
    };

    public String getResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "Hello! How can I help you today?";
        }
        String normalized = input.trim().toLowerCase();
        for (Map.Entry<List<String>, String[]> entry : responses.entrySet()) {
            for (String keyword : entry.getKey()) {
                if (normalized.contains(keyword.toLowerCase())) {
                    String[] options = entry.getValue();
                    return options[ThreadLocalRandom.current().nextInt(options.length)];
                }
            }
        }
        return fallbacks[ThreadLocalRandom.current().nextInt(fallbacks.length)];
    }
}