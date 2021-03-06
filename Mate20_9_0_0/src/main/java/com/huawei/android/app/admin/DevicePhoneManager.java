package com.huawei.android.app.admin;

import android.content.ComponentName;
import android.content.Context;
import android.location.Country;
import android.location.CountryDetector;
import android.os.Bundle;
import android.telephony.CallerInfoHW;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import com.huawei.android.content.ContextEx;
import com.huawei.internal.telephony.PhoneConstantsEx;
import huawei.android.app.admin.HwDevicePolicyManagerEx;
import java.util.ArrayList;
import java.util.Iterator;

public class DevicePhoneManager {
    public static final String ACTION_MDM_CONFIG_CHANGED = "com.huawei.devicepolicy.action.POLICY_CHANGED";
    public static final int ALLOW_ALL_MODE = 3;
    public static final String APPLY_TO_ALL_INCOMING_CALLS = "apply-to-all-incoming-calls";
    public static final String APPLY_TO_ALL_OUTGOING_CALLS = "apply-to-all-outgoing-calls";
    public static final int BLOCK_ALL_MODE = 2;
    public static final int CALLS_LIMIT_TYPE_DAY = 0;
    public static final int CALLS_LIMIT_TYPE_MONTH = 2;
    public static final int CALLS_LIMIT_TYPE_WEEK = 1;
    public static final String CONFIG_EXTRA_ADMIN_REMOVED = "admin_removed";
    public static final String CONFIG_EXTRA_CALLS_LIMITATION = "calls_limit_removed";
    public static final String CONFIG_EXTRA_POLICY_NAME = "policy_name";
    public static final String CONFIG_EXTRA_VALUE = "value";
    public static final String DISALLOW_ACCESS_POINT_NAME = "disallow-access-point-name";
    public static final String DISALLOW_DATA_ROAMING = "disallow-data-roaming";
    public static final String DISALLOW_NON_EMERGENCY_CALL = "disallow-non-emergency-call";
    public static final String DISALLOW_ROAMING_CALL = "disallow-roaming-call";
    public static final String INCOMING_DAY_LIMIT = "incoming_day_limit";
    public static final String INCOMING_MONTH_LIMIT = "incoming_month_limit";
    public static final String INCOMING_WEEK_LIMIT = "incoming_week_limit";
    public static final int MATCH_ALL_MODE = 0;
    public static final int MATCH_PREFIX_MODE = 1;
    private static final int MAX_LIST_NUMBER_LIMIT = 200;
    public static final String OUTGOING_DAY_LIMIT = "outgoing_day_limit";
    public static final String OUTGOING_MONTH_LIMIT = "outgoing_month_limit";
    public static final String OUTGOING_WEEK_LIMIT = "outgoing_week_limit";
    public static final String PHONE_CALLS_LIMITATION = "calls_limitation";
    public static final String PHONE_CALLS_LIMITATION_INCOMING_DAY = "calls_limitation_incoming_day";
    public static final String PHONE_CALLS_LIMITATION_INCOMING_MONTH = "calls_limitation_incoming_month";
    public static final String PHONE_CALLS_LIMITATION_INCOMING_WEEK = "calls_limitation_incoming_week";
    public static final String PHONE_CALLS_LIMITATION_OUTGOING_DAY = "calls_limitation_outgoing_day";
    public static final String PHONE_CALLS_LIMITATION_OUTGOING_MONTH = "calls_limitation_outgoing_month";
    public static final String PHONE_CALLS_LIMITATION_OUTGOING_WEEK = "calls_limitation_outgoing_week";
    public static final String PHONE_NUMBER_INCOMING_LIST = "phone-number-incoming-list";
    public static final String PHONE_NUMBER_INCOMING_LIST_TYPE = "phone-number-incoming-is-black-list-mode";
    public static final String PHONE_NUMBER_OUTGOING_LIST = "phone-number-outgoing-list";
    public static final String PHONE_NUMBER_OUTGOING_LIST_TYPE = "phone-number-outgoing-is-black-list-mode";
    private static final String TAG = "DevicePhoneManager";
    private Context mContext;
    private final HwDevicePolicyManagerEx mDpm = new HwDevicePolicyManagerEx();
    private Country sCountryDetector;

    public DevicePhoneManager(Context context) {
        this.mContext = context;
    }

    public void hangupCalling(ComponentName admin) {
        this.mDpm.hangupCalling(admin);
    }

    public boolean setDataRoamingDisabled(ComponentName admin, boolean disable) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", disable);
        return this.mDpm.setPolicy(admin, DISALLOW_DATA_ROAMING, bundle);
    }

    public boolean isDataRoamingDisabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISALLOW_DATA_ROAMING);
        if (bundle == null) {
            return false;
        }
        return bundle.getBoolean("value", false);
    }

    public boolean setAccessPointNameDisabled(ComponentName admin, boolean disable) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", disable);
        return this.mDpm.setPolicy(admin, DISALLOW_ACCESS_POINT_NAME, bundle);
    }

    public boolean isApnChangeDisabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISALLOW_ACCESS_POINT_NAME);
        if (bundle == null) {
            return false;
        }
        return bundle.getBoolean("value", false);
    }

    public boolean setNonEmergencyCallDisabled(ComponentName admin, boolean disable) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", disable);
        return this.mDpm.setPolicy(admin, DISALLOW_NON_EMERGENCY_CALL, bundle);
    }

    public boolean isNonEmergencyCallDisabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISALLOW_NON_EMERGENCY_CALL);
        if (bundle == null) {
            return false;
        }
        return bundle.getBoolean("value", false);
    }

    public boolean setRoamingCallDisabled(ComponentName admin, boolean disable) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("value", disable);
        return this.mDpm.setPolicy(admin, DISALLOW_ROAMING_CALL, bundle);
    }

    public boolean isRoamingCallDisabled(ComponentName admin) {
        Bundle bundle = this.mDpm.getPolicy(admin, DISALLOW_ROAMING_CALL);
        if (bundle == null) {
            return false;
        }
        return bundle.getBoolean("value", false);
    }

    public boolean addMdmNumberList(ComponentName admin, ArrayList<String> numbers, int blockMode, boolean isOutgoing, boolean isBlackList) {
        boolean isBlockAllMode = blockMode == 2;
        boolean isAllowAllMode = blockMode == 3;
        if (isBlockAllMode) {
            isBlackList = true;
        } else if (isAllowAllMode) {
            isBlackList = false;
        } else if (!isValidPhoneNumbers(numbers)) {
            Log.e(TAG, "addMdmNumberList, invalid numbers, return false.");
            return false;
        } else if (isAddNumbersOutOfLimit(numbers.size(), isOutgoing)) {
            Log.e(TAG, "Add numbers out of limit, return false.");
            return false;
        } else {
            switch (blockMode) {
                case 0:
                    break;
                case 1:
                    updataNumbersForPrefixMode(numbers);
                    break;
                default:
                    Log.e(TAG, "addMdmNumberList unknow block mode, return false.");
                    return false;
            }
        }
        if (!setListTypePolicy(admin, isBlackList, isOutgoing)) {
            Log.e(TAG, "Set policy failed, return false.");
            return false;
        } else if (isBlockAllMode || isAllowAllMode) {
            String pName = isOutgoing ? APPLY_TO_ALL_OUTGOING_CALLS : APPLY_TO_ALL_INCOMING_CALLS;
            Bundle aBundle = new Bundle();
            aBundle.putBoolean("value", true);
            return this.mDpm.setPolicy(admin, pName, aBundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("value", numbers);
            return this.mDpm.setPolicy(admin, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST : PHONE_NUMBER_INCOMING_LIST, bundle);
        }
    }

    /* JADX WARNING: Missing block: B:17:0x0030, code skipped:
            return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isAddNumbersOutOfLimit(int size, boolean isOutgoing) {
        if (size > 200) {
            return true;
        }
        Bundle listBundle = this.mDpm.getPolicy(null, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST : PHONE_NUMBER_INCOMING_LIST);
        if (listBundle == null) {
            return false;
        }
        ArrayList<String> numbers = listBundle.getStringArrayList("value");
        if (numbers == null || numbers.isEmpty() || numbers.size() + size <= 200) {
            return false;
        }
        return true;
    }

    public boolean removeMdmNumberList(ComponentName admin, ArrayList<String> numbers, int blockMode, boolean isOutgoing, boolean removeAll) {
        if (removeAll) {
            return removeAllNumbers(admin, isOutgoing);
        }
        if (blockMode == 2 || blockMode == 3) {
            if (!this.mDpm.removePolicy(admin, isOutgoing ? APPLY_TO_ALL_OUTGOING_CALLS : APPLY_TO_ALL_INCOMING_CALLS, null)) {
                Log.e(TAG, "removeMdmNumberList, remove apply to all failed, return false.");
                return false;
            }
        } else if (isValidPhoneNumbers(numbers)) {
            switch (blockMode) {
                case 0:
                    break;
                case 1:
                    updataNumbersForPrefixMode(numbers);
                    break;
                default:
                    Log.e(TAG, "removeMdmNumberList unknow block mode, return false.");
                    return false;
            }
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("value", numbers);
            if (!this.mDpm.removePolicy(admin, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST : PHONE_NUMBER_INCOMING_LIST, bundle)) {
                Log.e(TAG, "removeMdmNumberList, remove list failed, return false.");
                return false;
            }
        } else {
            Log.e(TAG, "removeMdmNumberList, invalid numbers, return false.");
            return false;
        }
        return updateListTypePolicy(admin, isOutgoing);
    }

    private boolean updateListTypePolicy(ComponentName admin, boolean isOutgoing) {
        Bundle applyBundle = this.mDpm.getPolicy(null, isOutgoing ? APPLY_TO_ALL_OUTGOING_CALLS : APPLY_TO_ALL_INCOMING_CALLS);
        boolean z = false;
        if (applyBundle != null && applyBundle.getBoolean("value", false)) {
            return true;
        }
        String listPolicy = isOutgoing ? PHONE_NUMBER_OUTGOING_LIST : PHONE_NUMBER_INCOMING_LIST;
        Bundle listBundle = this.mDpm.getPolicy(null, listPolicy);
        if (listBundle != null) {
            ArrayList<String> numbers = listBundle.getStringArrayList("value");
            if (!(numbers == null || numbers.isEmpty())) {
                return true;
            }
        }
        if (this.mDpm.removePolicy(admin, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST_TYPE : PHONE_NUMBER_INCOMING_LIST_TYPE, null) && this.mDpm.removePolicy(admin, listPolicy, null)) {
            z = true;
        }
        return z;
    }

    public boolean isBlockNumber(ComponentName admin, String number, boolean isOutgoing) {
        if (!setListBefore(isOutgoing) || number == null) {
            return false;
        }
        Bundle bundle = this.mDpm.getPolicy(admin, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST_TYPE : PHONE_NUMBER_INCOMING_LIST_TYPE);
        boolean isBlackList = false;
        if (bundle != null) {
            isBlackList = bundle.getBoolean("value", true);
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isBlackList ");
        stringBuilder.append(isBlackList);
        Log.d(str, stringBuilder.toString());
        Bundle applyBundle = this.mDpm.getPolicy(admin, isOutgoing ? APPLY_TO_ALL_OUTGOING_CALLS : APPLY_TO_ALL_INCOMING_CALLS);
        if (applyBundle == null || !applyBundle.getBoolean("value", true)) {
            Bundle listBundle = this.mDpm.getPolicy(admin, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST : PHONE_NUMBER_INCOMING_LIST);
            if (listBundle == null) {
                Log.d(TAG, "Get block list bundle is null, return false.");
                return false;
            }
            ArrayList<String> numbers = listBundle.getStringArrayList("value");
            if (numbers == null || numbers.isEmpty()) {
                Log.d(TAG, "Get block list is empty, return false.");
                return false;
            } else if (isBlackList) {
                return phoneNumberMatchedInList(numbers, number);
            } else {
                return phoneNumberMatchedInList(numbers, number) ^ 1;
            }
        } else if (isBlackList) {
            return true;
        } else {
            return false;
        }
    }

    private boolean phoneNumberMatchedInList(ArrayList<String> list, String number) {
        String countryIso = getCountryIso(this.mContext);
        String standardNumber = makeStandardNumber(removePostDialPortion(number), countryIso);
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String numRecord = (String) it.next();
            if (numRecord.endsWith(PhoneConstantsEx.APN_TYPE_ALL)) {
                numRecord = numRecord.substring(0, numRecord.length() - 1);
                if (number.startsWith(numRecord)) {
                    return true;
                }
                String standardNumRecord = makeStandardNumber(numRecord, countryIso);
                if (!"".equals(standardNumRecord) && standardNumber.startsWith(standardNumRecord)) {
                    return true;
                }
            } else if (number.equals(numRecord) || standardNumber.equals(makeStandardNumber(numRecord, countryIso))) {
                return true;
            }
        }
        return false;
    }

    private String makeStandardNumber(String number, String countryIso) {
        String ret = iPHeadBarber(number);
        if (countryIso == null) {
            return ret;
        }
        String formatNum = PhoneNumberUtils.formatNumberToE164(ret, countryIso);
        if (formatNum != null) {
            ret = formatNum;
        }
        if (CallerInfoHW.getInstance().getIntlPrefixAndCCLen(ret) > 0) {
            ret = ret.substring(CallerInfoHW.getInstance().getIntlPrefixAndCCLen(ret));
        }
        return ret;
    }

    private boolean setListTypePolicy(ComponentName admin, boolean isBlackList, boolean isOutgoing) {
        String policyName = isOutgoing ? PHONE_NUMBER_OUTGOING_LIST_TYPE : PHONE_NUMBER_INCOMING_LIST_TYPE;
        if (!setListBefore(isOutgoing) || isListTypeValid(isBlackList, policyName)) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("value", isBlackList);
            return this.mDpm.setPolicy(admin, policyName, bundle);
        }
        Log.e(TAG, "Set policy list type is not valid, return false.");
        return false;
    }

    private boolean setListBefore(boolean isOutgoing) {
        Bundle applyBundle = this.mDpm.getPolicy(null, isOutgoing ? APPLY_TO_ALL_OUTGOING_CALLS : APPLY_TO_ALL_INCOMING_CALLS);
        if (applyBundle != null && applyBundle.getBoolean("value", false)) {
            return true;
        }
        Bundle listBundle = this.mDpm.getPolicy(null, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST : PHONE_NUMBER_INCOMING_LIST);
        if (listBundle != null) {
            ArrayList<String> numbers = listBundle.getStringArrayList("value");
            return (numbers == null || numbers.isEmpty()) ? false : true;
        }
    }

    private boolean isListTypeValid(boolean isBlackList, String policyName) {
        Bundle bundle = this.mDpm.getPolicy(null, policyName);
        boolean strState = false;
        if (bundle != null) {
            strState = bundle.getBoolean("value", isBlackList);
        }
        return strState == isBlackList;
    }

    private void updataNumbersForPrefixMode(ArrayList<String> numbers) {
        if (numbers != null && !numbers.isEmpty()) {
            int numSize = numbers.size();
            for (int i = 0; i < numSize; i++) {
                String num = (String) numbers.get(i);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(num);
                stringBuilder.append(PhoneConstantsEx.APN_TYPE_ALL);
                numbers.set(i, stringBuilder.toString());
            }
        }
    }

    private boolean isValidPhoneNumbers(ArrayList<String> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return false;
        }
        Iterator<String> it = numbers.iterator();
        while (it.hasNext()) {
            String num = (String) it.next();
            if (num == null || num.endsWith(PhoneConstantsEx.APN_TYPE_ALL)) {
                return false;
            }
        }
        return true;
    }

    private boolean removeAllNumbers(ComponentName admin, boolean isOutgoing) {
        return this.mDpm.removePolicy(admin, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST_TYPE : PHONE_NUMBER_INCOMING_LIST_TYPE, null) && this.mDpm.removePolicy(admin, isOutgoing ? APPLY_TO_ALL_OUTGOING_CALLS : APPLY_TO_ALL_INCOMING_CALLS, null) && this.mDpm.removePolicy(admin, isOutgoing ? PHONE_NUMBER_OUTGOING_LIST : PHONE_NUMBER_INCOMING_LIST, null);
    }

    private String getCountryIso(Context context) {
        if (this.sCountryDetector == null && context != null) {
            CountryDetector detector = (CountryDetector) context.getSystemService(ContextEx.COUNTRY_DETECTOR);
            if (detector != null) {
                this.sCountryDetector = detector.detectCountry();
            }
        }
        if (this.sCountryDetector == null) {
            return null;
        }
        return this.sCountryDetector.getCountryIso();
    }

    private String iPHeadBarber(String oriNumber) {
        String str = oriNumber;
        String result = str;
        String[] IPHEAD = new String[]{"17900", "17901", "17908", "17909", "11808", "17950", "17951", "12593", "17931", "17910", "17911", "17960", "17968", "17969", "10193"};
        int numberLen = oriNumber.length();
        if (numberLen < 5) {
            return str;
        }
        String ipHead = str.substring(0, 5);
        if (ipHead.equals(IPHEAD[0]) || ipHead.equals(IPHEAD[1]) || ipHead.equals(IPHEAD[2]) || ipHead.equals(IPHEAD[3]) || ipHead.equals(IPHEAD[4]) || ipHead.equals(IPHEAD[5]) || ipHead.equals(IPHEAD[6]) || ipHead.equals(IPHEAD[7]) || ipHead.equals(IPHEAD[8]) || ipHead.equals(IPHEAD[9]) || ipHead.equals(IPHEAD[10]) || ipHead.equals(IPHEAD[11]) || ipHead.equals(IPHEAD[12]) || ipHead.equals(IPHEAD[13]) || ipHead.equals(IPHEAD[14])) {
            result = str.substring(5, numberLen);
        }
        return result;
    }

    private int minPositive(int a, int b) {
        if (a >= 0 && b >= 0) {
            return a < b ? a : b;
        } else if (a >= 0) {
            return a;
        } else {
            if (b >= 0) {
                return b;
            }
            return -1;
        }
    }

    private int indexOfNetworkChar(String num) {
        return minPositive(num.indexOf(44), num.indexOf(59));
    }

    private String removePostDialPortion(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        int trimIndex = indexOfNetworkChar(phoneNumber);
        if (trimIndex == -1) {
            return phoneNumber;
        }
        return phoneNumber.substring(0, trimIndex);
    }

    public boolean setPhoneCallLimitation(ComponentName admin, boolean isOutgoing, int dateType, int limitNumber) {
        if (limitNumber < 0) {
            return false;
        }
        String policyName;
        String policyName2 = "";
        String bundleKey = "";
        switch (dateType) {
            case 0:
                policyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_DAY : PHONE_CALLS_LIMITATION_INCOMING_DAY;
                if (!isOutgoing) {
                    policyName2 = "incoming_day_limit";
                    break;
                }
                policyName2 = "outgoing_day_limit";
                break;
            case 1:
                policyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_WEEK : PHONE_CALLS_LIMITATION_INCOMING_WEEK;
                if (!isOutgoing) {
                    policyName2 = "incoming_week_limit";
                    break;
                }
                policyName2 = "outgoing_week_limit";
                break;
            case 2:
                policyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_MONTH : PHONE_CALLS_LIMITATION_INCOMING_MONTH;
                if (!isOutgoing) {
                    policyName2 = "incoming_month_limit";
                    break;
                }
                policyName2 = "outgoing_month_limit";
                break;
            default:
                Log.e(TAG, "setPhoneCallLimitation, date type error, return false.");
                return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString(policyName2, String.valueOf(limitNumber));
        return this.mDpm.setPolicy(admin, policyName, bundle);
    }

    public boolean removePhoneCallLimitation(ComponentName admin, boolean isOutgoing, int dateType) {
        String policyName = "";
        switch (dateType) {
            case 0:
                policyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_DAY : PHONE_CALLS_LIMITATION_INCOMING_DAY;
                break;
            case 1:
                policyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_WEEK : PHONE_CALLS_LIMITATION_INCOMING_WEEK;
                break;
            case 2:
                policyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_MONTH : PHONE_CALLS_LIMITATION_INCOMING_MONTH;
                break;
            default:
                Log.e(TAG, "removePhoneCallLimitation, date type error, return false.");
                return false;
        }
        return this.mDpm.removePolicy(admin, policyName, null);
    }

    public boolean isPhoneCallLimitationSet(ComponentName admin, boolean isOutgoing) {
        String dayPolicyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_DAY : PHONE_CALLS_LIMITATION_INCOMING_DAY;
        String dayBundleKey = isOutgoing ? "outgoing_day_limit" : "incoming_day_limit";
        Bundle dayBundle = this.mDpm.getPolicy(admin, dayPolicyName);
        if (dayBundle != null && dayBundle.getString(dayBundleKey) != null) {
            return true;
        }
        String weekPolicyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_WEEK : PHONE_CALLS_LIMITATION_INCOMING_WEEK;
        String weekBundleKey = isOutgoing ? "outgoing_week_limit" : "incoming_week_limit";
        Bundle weekBundle = this.mDpm.getPolicy(admin, weekPolicyName);
        if (weekBundle != null && weekBundle.getString(weekBundleKey) != null) {
            return true;
        }
        String monthPolicyName = isOutgoing ? PHONE_CALLS_LIMITATION_OUTGOING_MONTH : PHONE_CALLS_LIMITATION_INCOMING_MONTH;
        String monthBundleKey = isOutgoing ? "outgoing_month_limit" : "incoming_month_limit";
        Bundle monthBundle = this.mDpm.getPolicy(admin, monthPolicyName);
        if (monthBundle == null || monthBundle.getString(monthBundleKey) == null) {
            return false;
        }
        return true;
    }
}
