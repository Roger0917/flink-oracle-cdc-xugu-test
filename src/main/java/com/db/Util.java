package com.db;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Util {

        static final int AD1 = -719528;
        static final int YEAR_DAYS = 365;
        static final int YEAR = 110000;
        static final String NODE = ".";
        static final int ONE_THOUSAND = 1000;
        static final long AD2 = -62135596800000L;
        static final int NUM_4 = 4;
        static final int NUM_100 = 100;
        static final int NUM_400 = 400;
        static final String MARK_DUB_QUOTATION = "\"";
        static final String MYSQL_MARK_DUB_QUOTATION = "`";

        public final static int[][] DAY_MD = {
                {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {1, 7}, {1, 8}, {1, 9}, {1, 10}, {1, 11}, {1, 12}, {1, 13}, {1, 14}, {1, 15}, {1, 16},
                {1, 17}, {1, 18}, {1, 19}, {1, 20}, {1, 21}, {1, 22}, {1, 23}, {1, 24}, {1, 25}, {1, 26}, {1, 27}, {1, 28}, {1, 29}, {1, 30}, {1, 31},

                {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, {2, 7}, {2, 8}, {2, 9}, {2, 10}, {2, 11}, {2, 12}, {2, 13}, {2, 14}, {2, 15}, {2, 16},
                {2, 17}, {2, 18}, {2, 19}, {2, 20}, {2, 21}, {2, 22}, {2, 23}, {2, 24}, {2, 25}, {2, 26}, {2, 27}, {2, 28},

                {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {3, 7}, {3, 8}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {3, 13}, {3, 14}, {3, 15}, {3, 16},
                {3, 17}, {3, 18}, {3, 19}, {3, 20}, {3, 21}, {3, 22}, {3, 23}, {3, 24}, {3, 25}, {3, 26}, {3, 27}, {3, 28}, {3, 29}, {3, 30}, {3, 31},

                {4, 1}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {4, 10}, {4, 11}, {4, 12}, {4, 13}, {4, 14}, {4, 15}, {4, 16},
                {4, 17}, {4, 18}, {4, 19}, {4, 20}, {4, 21}, {4, 22}, {4, 23}, {4, 24}, {4, 25}, {4, 26}, {4, 27}, {4, 28}, {4, 29}, {4, 30},

                {5, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 5}, {5, 6}, {5, 7}, {5, 8}, {5, 9}, {5, 10}, {5, 11}, {5, 12}, {5, 13}, {5, 14}, {5, 15}, {5, 16},
                {5, 17}, {5, 18}, {5, 19}, {5, 20}, {5, 21}, {5, 22}, {5, 23}, {5, 24}, {5, 25}, {5, 26}, {5, 27}, {5, 28}, {5, 29}, {5, 30}, {5, 31},

                {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {6, 10}, {6, 11}, {6, 12}, {6, 13}, {6, 14}, {6, 15}, {6, 16},
                {6, 17}, {6, 18}, {6, 19}, {6, 20}, {6, 21}, {6, 22}, {6, 23}, {6, 24}, {6, 25}, {6, 26}, {6, 27}, {6, 28}, {6, 29}, {6, 30},

                {7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 9}, {7, 10}, {7, 11}, {7, 12}, {7, 13}, {7, 14}, {7, 15}, {7, 16},
                {7, 17}, {7, 18}, {7, 19}, {7, 20}, {7, 21}, {7, 22}, {7, 23}, {7, 24}, {7, 25}, {7, 26}, {7, 27}, {7, 28}, {7, 29}, {7, 30}, {7, 31},

                {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {8, 7}, {8, 8}, {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}, {8, 14}, {8, 15}, {8, 16},
                {8, 17}, {8, 18}, {8, 19}, {8, 20}, {8, 21}, {8, 22}, {8, 23}, {8, 24}, {8, 25}, {8, 26}, {8, 27}, {8, 28}, {8, 29}, {8, 30}, {8, 31},

                {9, 1}, {9, 2}, {9, 3}, {9, 4}, {9, 5}, {9, 6}, {9, 7}, {9, 8}, {9, 9}, {9, 10}, {9, 11}, {9, 12}, {9, 13}, {9, 14}, {9, 15}, {9, 16},
                {9, 17}, {9, 18}, {9, 19}, {9, 20}, {9, 21}, {9, 22}, {9, 23}, {9, 24}, {9, 25}, {9, 26}, {9, 27}, {9, 28}, {9, 29}, {9, 30},

                {10, 1}, {10, 2}, {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {10, 9}, {10, 10}, {10, 11}, {10, 12}, {10, 13}, {10, 14}, {10, 15}, {10, 16},
                {10, 17}, {10, 18}, {10, 19}, {10, 20}, {10, 21}, {10, 22}, {10, 23}, {10, 24}, {10, 25}, {10, 26}, {10, 27}, {10, 28}, {10, 29}, {10, 30}, {10, 31},

                {11, 1}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {11, 6}, {11, 7}, {11, 8}, {11, 9}, {11, 10}, {11, 11}, {11, 12}, {11, 13}, {11, 14}, {11, 15}, {11, 16},
                {11, 17}, {11, 18}, {11, 19}, {11, 20}, {11, 21}, {11, 22}, {11, 23}, {11, 24}, {11, 25}, {11, 26}, {11, 27}, {11, 28}, {11, 29}, {11, 30},

                {12, 1}, {12, 2}, {12, 3}, {12, 4}, {12, 5}, {12, 6}, {12, 7}, {12, 8}, {12, 9}, {12, 10}, {12, 11}, {12, 12}, {12, 13}, {12, 14}, {12, 15}, {12, 16},
                {12, 17}, {12, 18}, {12, 19}, {12, 20}, {12, 21}, {12, 22}, {12, 23}, {12, 24}, {12, 25}, {12, 26}, {12, 27}, {12, 28}, {12, 29}, {12, 30}, {12, 31},
        };

        public final static int[][] DAY_MD_RN = {
                {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, {1, 7}, {1, 8}, {1, 9}, {1, 10}, {1, 11}, {1, 12}, {1, 13}, {1, 14}, {1, 15}, {1, 16},
                {1, 17}, {1, 18}, {1, 19}, {1, 20}, {1, 21}, {1, 22}, {1, 23}, {1, 24}, {1, 25}, {1, 26}, {1, 27}, {1, 28}, {1, 29}, {1, 30}, {1, 31},

                {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, {2, 7}, {2, 8}, {2, 9}, {2, 10}, {2, 11}, {2, 12}, {2, 13}, {2, 14}, {2, 15}, {2, 16},
                {2, 17}, {2, 18}, {2, 19}, {2, 20}, {2, 21}, {2, 22}, {2, 23}, {2, 24}, {2, 25}, {2, 26}, {2, 27}, {2, 28}, {2, 29},

                {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {3, 7}, {3, 8}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {3, 13}, {3, 14}, {3, 15}, {3, 16},
                {3, 17}, {3, 18}, {3, 19}, {3, 20}, {3, 21}, {3, 22}, {3, 23}, {3, 24}, {3, 25}, {3, 26}, {3, 27}, {3, 28}, {3, 29}, {3, 30}, {3, 31},

                {4, 1}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {4, 10}, {4, 11}, {4, 12}, {4, 13}, {4, 14}, {4, 15}, {4, 16},
                {4, 17}, {4, 18}, {4, 19}, {4, 20}, {4, 21}, {4, 22}, {4, 23}, {4, 24}, {4, 25}, {4, 26}, {4, 27}, {4, 28}, {4, 29}, {4, 30},

                {5, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 5}, {5, 6}, {5, 7}, {5, 8}, {5, 9}, {5, 10}, {5, 11}, {5, 12}, {5, 13}, {5, 14}, {5, 15}, {5, 16},
                {5, 17}, {5, 18}, {5, 19}, {5, 20}, {5, 21}, {5, 22}, {5, 23}, {5, 24}, {5, 25}, {5, 26}, {5, 27}, {5, 28}, {5, 29}, {5, 30}, {5, 31},

                {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8}, {6, 9}, {6, 10}, {6, 11}, {6, 12}, {6, 13}, {6, 14}, {6, 15}, {6, 16},
                {6, 17}, {6, 18}, {6, 19}, {6, 20}, {6, 21}, {6, 22}, {6, 23}, {6, 24}, {6, 25}, {6, 26}, {6, 27}, {6, 28}, {6, 29}, {6, 30},

                {7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {7, 8}, {7, 9}, {7, 10}, {7, 11}, {7, 12}, {7, 13}, {7, 14}, {7, 15}, {7, 16},
                {7, 17}, {7, 18}, {7, 19}, {7, 20}, {7, 21}, {7, 22}, {7, 23}, {7, 24}, {7, 25}, {7, 26}, {7, 27}, {7, 28}, {7, 29}, {7, 30}, {7, 31},

                {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {8, 7}, {8, 8}, {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}, {8, 14}, {8, 15}, {8, 16},
                {8, 17}, {8, 18}, {8, 19}, {8, 20}, {8, 21}, {8, 22}, {8, 23}, {8, 24}, {8, 25}, {8, 26}, {8, 27}, {8, 28}, {8, 29}, {8, 30}, {8, 31},

                {9, 1}, {9, 2}, {9, 3}, {9, 4}, {9, 5}, {9, 6}, {9, 7}, {9, 8}, {9, 9}, {9, 10}, {9, 11}, {9, 12}, {9, 13}, {9, 14}, {9, 15}, {9, 16},
                {9, 17}, {9, 18}, {9, 19}, {9, 20}, {9, 21}, {9, 22}, {9, 23}, {9, 24}, {9, 25}, {9, 26}, {9, 27}, {9, 28}, {9, 29}, {9, 30},

                {10, 1}, {10, 2}, {10, 3}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {10, 9}, {10, 10}, {10, 11}, {10, 12}, {10, 13}, {10, 14}, {10, 15}, {10, 16},
                {10, 17}, {10, 18}, {10, 19}, {10, 20}, {10, 21}, {10, 22}, {10, 23}, {10, 24}, {10, 25}, {10, 26}, {10, 27}, {10, 28}, {10, 29}, {10, 30}, {10, 31},

                {11, 1}, {11, 2}, {11, 3}, {11, 4}, {11, 5}, {11, 6}, {11, 7}, {11, 8}, {11, 9}, {11, 10}, {11, 11}, {11, 12}, {11, 13}, {11, 14}, {11, 15}, {11, 16},
                {11, 17}, {11, 18}, {11, 19}, {11, 20}, {11, 21}, {11, 22}, {11, 23}, {11, 24}, {11, 25}, {11, 26}, {11, 27}, {11, 28}, {11, 29}, {11, 30},

                {12, 1}, {12, 2}, {12, 3}, {12, 4}, {12, 5}, {12, 6}, {12, 7}, {12, 8}, {12, 9}, {12, 10}, {12, 11}, {12, 12}, {12, 13}, {12, 14}, {12, 15}, {12, 16},
                {12, 17}, {12, 18}, {12, 19}, {12, 20}, {12, 21}, {12, 22}, {12, 23}, {12, 24}, {12, 25}, {12, 26}, {12, 27}, {12, 28}, {12, 29}, {12, 30}, {12, 31},
        };

        public static byte getByte(byte[] data) {
            return new Byte(data[0]);
        }


        public static short getShort(byte[] data) {
            return (short) ((data[0] & 0xFF) | ((data[1] & 0xFF) << 8));
        }

        public static int getInt(byte[] data) {
            return (data[0] & 0xFF) | ((data[1] & 0xFF) << 8)
                    | ((data[2] & 0xFF) << 16) | ((data[3] & 0xFF) << 24);
        }

        public static long getLong(byte[] data) {
            return (long) ((long) data[0] & 0xFF) | (((long) data[1] & 0xFF) << 8)
                    | (((long) data[2] & 0xFF) << 16) | (((long) data[3] & 0xFF) << 24)
                    | (((long) data[4] & 0xFF) << 32) | (((long) data[5] & 0xFF) << 40)
                    | (((long) data[6] & 0xFF) << 48) | (((long) data[7] & 0xFF) << 56);
        }

        public static float getFloat(byte[] data) {
            int tmpI = (data[0] & 0xFF) | ((data[1] & 0xFF) << 8)
                    | ((data[2] & 0xFF) << 16) | ((data[3] & 0xFF) << 24);
            return Float.intBitsToFloat(tmpI);
        }

        public static double getDouble(byte[] data) {
            long tmpL = ((long) data[0] & 0xFF) | (((long) data[1] & 0xFF) << 8)
                    | (((long) data[2] & 0xFF) << 16) | (((long) data[3] & 0xFF) << 24)
                    | (((long) data[4] & 0xFF) << 32) | (((long) data[5] & 0xFF) << 40)
                    | (((long) data[6] & 0xFF) << 48) | (((long) data[7] & 0xFF) << 56);
            return Double.longBitsToDouble(tmpL);
        }

        public static byte getByte(byte[] data, int off, int len) {
            checkBounds(data, off, len);
            return new Byte(data[off]);
        }

        public static int getTiny(byte[] data, int off) {
            checkBounds(data, off, 1);
            return (int) (data[off]);
        }

        public static short getShort(byte[] data, int off, int len) {
            checkBounds(data, off, len);
            return (short) ((data[off] & 0xFF) | ((data[off + 1] & 0xFF) << 8));
        }

        public static short getShort(byte[] data, int off) {
            checkBounds(data, off, 2);
            return (short) ((data[off] & 0xFF) | ((data[off + 1] & 0xFF) << 8));
        }

        public static int getInt(byte[] data, int off, int len) {
            checkBounds(data, off, len);
            return (data[off] & 0xFF) | ((data[off + 1] & 0xFF) << 8)
                    | ((data[off + 2] & 0xFF) << 16) | ((data[off + 3] & 0xFF) << 24);
        }

        public static int getInt(byte[] data, int off) {
            checkBounds(data, off, 4);
            return (data[off] & 0xFF) | ((data[off + 1] & 0xFF) << 8)
                    | ((data[off + 2] & 0xFF) << 16) | ((data[off + 3] & 0xFF) << 24);
        }

        public static long getLong(byte[] data, int off, int len) {
            checkBounds(data, off, len);
            return ((long) data[off] & 0xFF) | (((long) data[off + 1] & 0xFF) << 8)
                    | (((long) data[off + 2] & 0xFF) << 16) | (((long) data[off + 3] & 0xFF) << 24)
                    | (((long) data[off + 4] & 0xFF) << 32) | (((long) data[off + 5] & 0xFF) << 40)
                    | (((long) data[off + 6] & 0xFF) << 48) | (((long) data[off + 7] & 0xFF) << 56);
        }

        public static long getLong(byte[] data, int off) {
            checkBounds(data, off, 8);
            return ((long) data[off] & 0xFF) | (((long) data[off + 1] & 0xFF) << 8)
                    | (((long) data[off + 2] & 0xFF) << 16) | (((long) data[off + 3] & 0xFF) << 24)
                    | (((long) data[off + 4] & 0xFF) << 32) | (((long) data[off + 5] & 0xFF) << 40)
                    | (((long) data[off + 6] & 0xFF) << 48) | (((long) data[off + 7] & 0xFF) << 56);
        }

        public static float getFloat(byte[] data, int off, int len) {
            int tmpI = getInt(data, off, len);
            return Float.intBitsToFloat(tmpI);
        }

        public static double getDouble(byte[] data, int off, int len) {
            long tmpL = getLong(data, off, len);
            return Double.longBitsToDouble(tmpL);
        }

        public static String getString(byte[] data, int off, int len) {
            checkBounds(data, off, len);
            return new String(data, off, len);
        }

        public static String getString(byte[] data, int off, int len, String charSet) throws UnsupportedEncodingException {
            checkBounds(data, off, len);
            return new String(data, off, len, charSet);
        }

        private static void checkBounds(byte[] data, int off, int len) {
            if (len < 0) {
                throw new StringIndexOutOfBoundsException(len);
            }
            if (off < 0) {
                throw new StringIndexOutOfBoundsException(off);
            }
            if (off > data.length - len) {
                throw new StringIndexOutOfBoundsException(off + len);
            }
        }

        public static String getDate(int data) {
            XgDate xDate = new XgDate();
            // 定义了年\月\日变量
            int y, d, ms, d1;
            int s;
            long tt, tt1;
            boolean isAD = true;
            tt1 = data;
            s = 0;
            ms = 0;
            d = (int) tt1;
            if (tt1 <= AD1) {
                isAD = false;
            }

            d += 40896202;
            d1 = d - 10;
            y = (d1 / 146097) * 400;
            d1 %= 146097;

            y += (d1 / 36524) * 100;
            d1 %= 36524;

            y += (d1 / 1461) * 4;
            d1 %= 1461;
            y += d1 / 365;

            // 反算到y年1月1号对应的天数
            d1 = y * 365 + (y - 1) / 4 - (y - 1) / 100 + (y - 1) / 400;
            d -= d1;

            if (d >= YEAR_DAYS) {
                if (isRn(y)) {
                    if (d > YEAR_DAYS) {
                        y++;
                        d -= 366;
                    }
                } else {
                    y++;
                    d -= 365;
                }
            }
            if (y - YEAR < 1) {
                xDate.year = 1 - (y - 110000);

            } else {
                xDate.year = y - 110000;
            }

            if (isRn((int) y)) {
                xDate.month = DAY_MD_RN[d][0];
                xDate.day = DAY_MD_RN[d][1];

            } else {
                xDate.month = DAY_MD[d][0];
                xDate.day = DAY_MD[d][1];

            }
            return xDate.getDateString();

        }

        public static String getTime(Long data) {
            XgDate xDate = new XgDate();
            int ms, s;
            long tt, tt1;
            tt1 = data;
            ms = (int) (tt1 % 1000);
            tt = tt1 / 1000000;
            if (tt1 < 0) {
                if (ms != 0) {
                    ms += 1000;
                    tt--;
                }
                s = (int) (tt % 86400);
                if (s != 0) {
                    s += 86400;
                }
            } else {
                s = (int) (tt % 86400);
            }

            xDate.hour = s / 3600;
            s %= 3600;
            xDate.mi = s / 60;
            xDate.sec = s % 60;
            xDate.usec = ms * 1000;
            return xDate.getTime();

        }

        public static String getTz(short tz) {
            if (tz > 0) {
                return " +" + tz / 60 + ":" + tz % 60;
            } else {
                short n = (short) (tz - 2 * tz);
                return " -" + n / 60 + ":" + n % 60;
            }
        }

        public static String getDateTime(long data) {
            XgDate xDate = new XgDate();
            boolean isAD = true;
            long t = 0;
            int dateT = 0;
            int s, m, h;
            t = data;
            if (t / ONE_THOUSAND < AD2) {
                isAD = false;
            }

            dt2tm(t, xDate, false);
            if (xDate.year < 1) {
                xDate.year = 1 - xDate.year;
            }
            s = xDate.sec;

            h = s / 3600;
            m = s / 60;
            s %= 60;

            xDate.hour = h;
            xDate.mi = m % 60;
            xDate.sec = s;
            return xDate.getDtString();
        }

        public static void dt2tm(long t, XgDate xDate, boolean hasTimeZone) {
            int y, d, d1, s, us;    //用来存放年月日等

            if (!hasTimeZone) {
                t += 3533431852800000000L;
            }
            us = (int) (t % 1000000);
            t = t / 1000000;
            s = (int) (t % 86400);
            d = (int) (t / 86400);

            d1 = d - 10;
            y = (d1 / 146097) * 400;
            d1 %= 146097;

            y += (d1 / 36524) * 100;
            d1 %= 36524;

            y += (d1 / 1461) * 4;
            d1 %= 1461;

            y += d1 / 365;
            //反算到y年1月1号对应的天数
            d1 = y * 365 + (y - 1) / 4 - (y - 1) / 100 + (y - 1) / 400;
            d -= d1;

            if (d >= YEAR_DAYS) {
                if (y % NUM_400 == 0 || (y % NUM_4 == 0 && y % NUM_100 != 0)) {
                    if (d > YEAR_DAYS) {
                        y++;
                        d -= 366;
                    }
                } else {
                    y++;
                    d -= 365;
                }
            }

            xDate.year = y - 110000;
            if (y % NUM_400 == 0 || (y % NUM_4 == 0 && y % NUM_100 != 0)) {
                xDate.month = DAY_MD_RN[d][0];
                xDate.day = DAY_MD_RN[d][1];
            } else {
                xDate.month = DAY_MD[d][0];
                xDate.day = DAY_MD[d][1];
            }
            xDate.sec = s;
            xDate.usec = us;
        }

        public static boolean isRn(int y) {
            boolean f;
            if (y % NUM_400 == 0 || (y % NUM_4 == 0 && y % NUM_100 != 0)) {
                f = true;
            } else {
                f = false;
            }
            return f;
        }

        public static int getLobSize(byte[] data) {
            int tmpI = (data[0] & 0xFF) |
                    ((data[1] & 0xFF) << 8) |
                    ((data[2] & 0xFF) << 16) |
                    ((data[3] & 0xFF) << 24);
            return tmpI;
        }

        public static byte[] int2Byte4Be(int i) {
            byte[] bb = new byte[4];
            bb[0] = (byte) (i >> 24 & 0xFF);
            bb[1] = (byte) (i >> 16 & 0xFF);
            bb[2] = (byte) (i >> 8 & 0xFF);
            bb[3] = (byte) (i & 0xFF);
            return bb;
        }

        public static byte[] int2Byte4Le(int i) {
            byte[] bb = new byte[4];
            bb[0] = (byte) (i & 0xFF);
            bb[1] = (byte) (i >> 8 & 0xFF);
            bb[2] = (byte) (i >> 16 & 0xFF);
            bb[3] = (byte) (i >> 24 & 0xFF);
            return bb;
        }

        public static byte[] long2Byte8Be(long i) {
            byte[] bb = new byte[8];
            bb[0] = (byte) (i >> 56 & 0xFF);
            bb[1] = (byte) (i >> 48 & 0xFF);
            bb[2] = (byte) (i >> 40 & 0xFF);
            bb[3] = (byte) (i >> 32 & 0xFF);
            bb[4] = (byte) (i >> 24 & 0xFF);
            bb[5] = (byte) (i >> 16 & 0xFF);
            bb[6] = (byte) (i >> 8 & 0xFF);
            bb[7] = (byte) (i & 0xFF);
            return bb;
        }

        public static byte[] long2Byte8Le(long i) {
            byte[] bb = new byte[8];
            bb[7] = (byte) (i >> 56 & 0xFF);
            bb[6] = (byte) (i >> 48 & 0xFF);
            bb[5] = (byte) (i >> 40 & 0xFF);
            bb[4] = (byte) (i >> 32 & 0xFF);
            bb[3] = (byte) (i >> 24 & 0xFF);
            bb[2] = (byte) (i >> 16 & 0xFF);
            bb[1] = (byte) (i >> 8 & 0xFF);
            bb[0] = (byte) (i & 0xFF);
            return bb;
        }

        public static byte[] int2Byte2Le(int i) {
            byte[] bb = new byte[2];
            bb[0] = (byte) (i & 0xFF);
            bb[1] = (byte) (i >> 8 & 0xFF);
            return bb;
        }

        public static byte[] int2Byte2Be(int i) {
            byte[] bb = new byte[2];
            bb[0] = (byte) (i >> 8 & 0xFF);
            bb[1] = (byte) (i & 0xFF);
            return bb;
        }

        public static byte[] int2Byte1(int i) {
            byte[] bb = new byte[1];
            bb[0] = (byte) (i & 0xFF);
            return bb;
        }

        public static long ip2Long(String strIp) {
            long[] ip = new long[4];
            String[] ips = strIp.split("\\.");
            ip[0] = Long.parseLong(ips[0]);
            ip[1] = Long.parseLong(ips[1]);
            ip[2] = Long.parseLong(ips[2]);
            ip[3] = Long.parseLong(ips[3]);
            return (ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | ip[3];
        }

        public static String long2Ip(long longIp) {
            StringBuffer sb = new StringBuffer("");
            sb.append(String.valueOf((longIp >>> 24)));
            sb.append(".");
            sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
            sb.append(".");
            sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
            sb.append(".");
            sb.append(String.valueOf((longIp & 0x000000FF)));
            return sb.toString();
        }

        public static String long2DateStr(long time) {
            String ret = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ret = sdf.format(new Date(time));
            return ret;
        }

        public static long dateStr2Long(String dateStr) throws ParseException {
            long ret = 0L;
            if (dateStr.lastIndexOf(NODE) > 0) {
                dateStr = dateStr.substring(0, dateStr.lastIndexOf("."));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ret = sdf.parse(dateStr).getTime();
            return ret;
        }

        /**
         * 获取数据库对象包裹串
         */
        public static String QuotaDatabaseObjectDub(String objectName) {
            if (SynToDbApplication.type.equals("mysql")) {
                return MYSQL_MARK_DUB_QUOTATION + objectName + MYSQL_MARK_DUB_QUOTATION;
            } else {
                return MARK_DUB_QUOTATION + objectName + MARK_DUB_QUOTATION;
            }
        }

        public static String gbEncoding(final String gbString) {
            char[] utfBytes = gbString.toCharArray();
            String unicodeBytes = "";
            for (int i = 0; i < utfBytes.length; i++) {
                String hexB = Integer.toHexString(utfBytes[i]);
                if (hexB.length() <= 2) {
                    hexB = "00" + hexB;
                }
                unicodeBytes = unicodeBytes + "\\u" + hexB;
            }
            return unicodeBytes;
        }

        public static String decodeUnicode(final String dataStr) {
            int start = 0;
            int end = 0;
            final StringBuffer buffer = new StringBuffer();
            while (start > -1) {
                end = dataStr.indexOf("\\u", start + 2);
                String charStr = "";
                if (end == -1) {
                    charStr = dataStr.substring(start + 2, dataStr.length());
                } else {
                    charStr = dataStr.substring(start + 2, end);
                }
                char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
                buffer.append(new Character(letter).toString());
                start = end;
            }
            return buffer.toString();
        }
    }
